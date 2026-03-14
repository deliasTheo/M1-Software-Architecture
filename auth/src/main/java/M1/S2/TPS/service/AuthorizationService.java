package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.Role;
import M1.S2.TPS.exception.InsufficientPermissionsException;
import M1.S2.TPS.exception.ServiceNotFoundException;
import M1.S2.TPS.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    
    private final TokenService tokenService;
    private final AuthorityRepository authorityRepository;

    public void validateAccess(String authorizationHeader, String targetService) {
        // Check if the service exists
        if (!authorityRepository.existsByCode(targetService.toUpperCase())) {
            throw new ServiceNotFoundException(targetService);
        }

        String token = TokenService.extractBearerToken(authorizationHeader);
        // Get identity from token (throws InvalidTokenException if invalid/expired)
        Identity identity = tokenService.getIdentityBySessionToken(token);

        tokenService.verifySessionToken(identity, token);    

        // Check if user has access to the service
        if (!hasAccessToService(identity, targetService)) {
            throw new InsufficientPermissionsException(targetService);
        }
        
    }

    private boolean hasAccessToService(Identity identity, String targetService) {
        return identity.getAuthorities().stream()
                .anyMatch(authority -> authority.getCode().equalsIgnoreCase(targetService));
    }

    public Role verifyRole(String authorizationHeader) {
        String token = TokenService.extractBearerToken(authorizationHeader);

        Identity identity = tokenService.getIdentityBySessionToken(token);

        return identity.getRole();
    }
}
