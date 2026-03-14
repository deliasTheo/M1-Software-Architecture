package M1.S2.TPS.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.exception.EmailAlreadyExistsException;
import M1.S2.TPS.exception.EmailNotVerifiedException;
import M1.S2.TPS.exception.InvalidCredentialsException;
import M1.S2.TPS.exception.InvalidDataException;
import M1.S2.TPS.entities.ValidationToken;
import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.SessionToken;
import M1.S2.TPS.repository.CredentialRepository;
import M1.S2.TPS.repository.IdentityRepository;
import M1.S2.TPS.repository.SessionTokenRepository;
import M1.S2.TPS.repository.ValidationTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    
     /*
        register(...)

        login(...)

        createSessionToken(...)

        checkPassword(...)
    */

    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final EmailVerificationService emailVerificationService;
    private final IdentityRepository identityRepository;
    private final CredentialRepository credentialRepository;
    private final ValidationTokenRepository validationTokenRepository;
    private final SessionTokenRepository sessionTokenRepository;
   
    @Transactional
    public void register(IdentityDTO identityDTO) {
        // validate input data
        if (identityDTO.getEmail() == null || identityDTO.getEmail().isBlank()) {
            throw new InvalidDataException("L'email est requis");
        }
        if (identityDTO.getPassword() == null || identityDTO.getPassword().isBlank()) {
            throw new InvalidDataException("Le mot de passe est requis");
        }
        if (!identityDTO.getEmail().matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$")) {
            throw new InvalidDataException("Format d'email invalide");
        }

        // check if email already exists
        if (identityRepository.existsByEmail(identityDTO.getEmail())) {
            throw new EmailAlreadyExistsException(identityDTO.getEmail());
        }

        String hashedPassword = passwordService.hashPassword(identityDTO.getPassword());
       
        // create identity
        Identity identity = new Identity();
        identity.setEmail(identityDTO.getEmail());
        identity.setUid(UUID.randomUUID().toString());

        // save identity first to get the ID
        identityRepository.save(identity);

        // create credential with persisted identity
        Credential credential = new Credential();
        credential.setIdentity(identity);
        credential.setPasswordHash(hashedPassword);
        credentialRepository.save(credential);

        identity.setCredential(credential);

        // create and save validation token
        ValidationToken validationToken = tokenService.createValidationToken(identity);
        validationTokenRepository.save(validationToken);

        // publish user registered event
        emailVerificationService.publishUserRegisteredEvent(identity);


    }
        
    public SessionToken login(IdentityDTO identityDTO) {
        // find identity by email
        Identity identity = identityRepository.findByEmail(identityDTO.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // verify password
        Credential credential = identity.getCredential();
        if (credential == null || !passwordService.verifyPassword(identity, identityDTO.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // check if email is verified
        if (!identity.isVerified()) {
            throw new EmailNotVerifiedException();
        }

        // create and return session token
        SessionToken token = tokenService.createSessionToken(identity);
        sessionTokenRepository.save(token);
        return token;

    }

    public void logout(String token) {
        SessionToken sessionToken = tokenService.getSessionToken(token);
        sessionTokenRepository.delete(sessionToken);
    }
    
}
