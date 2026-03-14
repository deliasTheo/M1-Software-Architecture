package M1.S2.TPS.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import M1.S2.TPS.client.AuthServiceClient;
import M1.S2.TPS.dto.CreateUserRequest;
import M1.S2.TPS.dto.DeleteUserRequest;
import M1.S2.TPS.dto.ModifyRoleRequest;
import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.Role;
import M1.S2.TPS.entities.ValidationToken;
import M1.S2.TPS.exception.EmailAlreadyExistsException;
import M1.S2.TPS.exception.ForbiddenOperationException;
import M1.S2.TPS.exception.InvalidDataException;
import M1.S2.TPS.exception.UnauthorizedException;
import M1.S2.TPS.exception.UserNotFoundException;
import M1.S2.TPS.messaging.AdminEventPublisher;
import M1.S2.TPS.messaging.UserRegisteredEvent;
import M1.S2.TPS.repository.CredentialRepository;
import M1.S2.TPS.repository.IdentityRepository;
import M1.S2.TPS.repository.RoleRepository;
import M1.S2.TPS.repository.ValidationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AuthServiceClient authServiceClient;
    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final IdentityRepository identityRepository;
    private final CredentialRepository credentialRepository;
    private final RoleRepository roleRepository;
    private final ValidationTokenRepository validationTokenRepository;
    private final AdminEventPublisher eventPublisher;

    private static final String ROLE_ADMIN = "admin";
    private static final String ROLE_MODO = "modo";
    private static final String ROLE_USER = "user";

    @Transactional
    public void addUser(String authorizationHeader, CreateUserRequest request) {
        String callerRole = verifyCallerRole(authorizationHeader);
        
        if (!isAdminOrModo(callerRole)) {
            throw new UnauthorizedException("Seuls les admins et modos peuvent ajouter des utilisateurs");
        }

        validateCreateUserRequest(request);

        String targetRole = request.getRole() != null ? request.getRole().toLowerCase() : ROLE_USER;
        
        if (ROLE_ADMIN.equals(targetRole)) {
            throw new ForbiddenOperationException("Impossible de créer un autre admin");
        }
        
        if (ROLE_MODO.equals(targetRole) && !ROLE_ADMIN.equals(callerRole)) {
            throw new ForbiddenOperationException("Seul l'admin peut créer un modo");
        }

        if (identityRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        Role role = roleRepository.findByName(targetRole)
                .orElseThrow(() -> new InvalidDataException("Rôle invalide: " + targetRole));

        String hashedPassword = passwordService.hashPassword(request.getPassword());

        Identity identity = new Identity();
        identity.setEmail(request.getEmail());
        identity.setUid(UUID.randomUUID().toString());
        identity.setRole(role);
        identity.setVerified(false);
        identityRepository.save(identity);

        Credential credential = new Credential();
        credential.setIdentity(identity);
        credential.setPasswordHash(hashedPassword);
        credentialRepository.save(credential);

        identity.setCredential(credential);

        ValidationToken validationToken = tokenService.createValidationToken(identity);
        validationTokenRepository.save(validationToken);

        publishUserCreatedEvent(identity, validationToken.getTokenHash());

        log.info("Utilisateur créé par {}: {} avec rôle {}", callerRole, request.getEmail(), targetRole);
    }

    @Transactional
    public void deleteUser(String authorizationHeader, DeleteUserRequest request) {
        String callerRole = verifyCallerRole(authorizationHeader);
        
        if (!isAdminOrModo(callerRole)) {
            throw new UnauthorizedException("Seuls les admins et modos peuvent supprimer des utilisateurs");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidDataException("L'email est requis");
        }

        Identity targetUser = identityRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        String targetRole = targetUser.getRole().getName();

        if (ROLE_ADMIN.equals(targetRole)) {
            throw new ForbiddenOperationException("Impossible de supprimer l'admin");
        }

        if (ROLE_MODO.equals(callerRole) && (ROLE_MODO.equals(targetRole) || ROLE_ADMIN.equals(targetRole))) {
            throw new ForbiddenOperationException("Un modo ne peut pas supprimer un altro modo ou l'admin");
        }

        identityRepository.delete(targetUser);
        log.info("Utilisateur supprimé par {}: {}", callerRole, request.getEmail());
    }

    @Transactional
    public void modifyRole(String authorizationHeader, ModifyRoleRequest request) {
        String callerRole = verifyCallerRole(authorizationHeader);
        
        if (!ROLE_ADMIN.equals(callerRole)) {
            throw new UnauthorizedException("Seul l'admin peut modifier les rôles");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidDataException("L'email est requis");
        }
        if (request.getNewRole() == null || request.getNewRole().isBlank()) {
            throw new InvalidDataException("Le nouveau rôle est requis");
        }

        String newRole = request.getNewRole().toLowerCase();

        if (ROLE_ADMIN.equals(newRole)) {
            throw new ForbiddenOperationException("Impossible de promouvoir quelqu'un en admin");
        }

        Identity targetUser = identityRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail()));

        if (ROLE_ADMIN.equals(targetUser.getRole().getName())) {
            throw new ForbiddenOperationException("Impossible de modifier le rôle de l'admin");
        }

        Role role = roleRepository.findByName(newRole)
                .orElseThrow(() -> new InvalidDataException("Rôle invalide: " + newRole));

        targetUser.setRole(role);
        targetUser.setUpdatedAt(LocalDateTime.now());
        identityRepository.save(targetUser);

        log.info("Rôle modifié pour {}: {} -> {}", request.getEmail(), targetUser.getRole().getName(), newRole);
    }

    private String verifyCallerRole(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new UnauthorizedException("Token manquant");
        }
        return authServiceClient.verifyRole(authorizationHeader);
    }

    private boolean isAdminOrModo(String role) {
        return ROLE_ADMIN.equals(role) || ROLE_MODO.equals(role);
    }

    private void validateCreateUserRequest(CreateUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidDataException("L'email est requis");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidDataException("Le mot de passe est requis");
        }
        if (!request.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            throw new InvalidDataException("Format d'email invalide");
        }
    }

    private void publishUserCreatedEvent(Identity identity, String tokenClear) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                "USER_REGISTERED",
                UUID.randomUUID().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                new UserRegisteredEvent.UserRegisteredData(
                        identity.getId(),
                        identity.getEmail(),
                        tokenClear
                )
        );
        eventPublisher.publishUserRegistered(event);
        log.info("Événement USER_REGISTERED publié pour {}", identity.getEmail());
    }
}
