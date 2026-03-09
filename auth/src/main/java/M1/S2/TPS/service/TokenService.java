package M1.S2.TPS.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.SessionToken;
import M1.S2.TPS.entities.ValidationToken;
import M1.S2.TPS.exception.InvalidTokenException;
import M1.S2.TPS.repository.SessionTokenRepository;
import M1.S2.TPS.repository.ValidationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final SessionTokenRepository sessionTokenRepository;
    private final ValidationTokenRepository validationTokenRepository;

    public ValidationToken createValidationToken(Identity identity) {
        ValidationToken validationToken = new ValidationToken();
        validationToken.setIdentity(identity);
        validationToken.setTokenHash(hashToken(generateToken()));
        validationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        return validationToken;
    }

    public SessionToken createSessionToken(Identity identity) {
        SessionToken sessionToken = new SessionToken();
        sessionToken.setIdentity(identity);
        sessionToken.setTokenHash(hashToken(generateToken()));
        sessionToken.setExpiresAt(LocalDateTime.now().plusHours(4));
        return sessionToken;
    }

    public boolean verifyToken(Identity identity, String token) {
        // vérifié que le token est calide est non expiré

            // utiliser getIdentityBySessionToken ou getIdentityByValidationToken
        // A utiliser, si el token est expirer : 
        //  if (isTokenExpired(sessionToken.getExpiresAt())) {
            // throw new InvalidTokenException();
        // }

        return true;
    }

     // todo : semy
    private String hashToken(String token) {
        return token;
    }

    // todo : semy
    private String generateToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    }

    public Identity getIdentityBySessionToken(String token) {
        SessionToken sessionToken = sessionTokenRepository.findByTokenHash(token)
                .orElseThrow(InvalidTokenException::new);
               
        return sessionToken.getIdentity();
    }

    public Identity getIdentityByValidationToken(String token) {
        ValidationToken validationToken = validationTokenRepository.findByTokenHash(token)
                .orElseThrow(InvalidTokenException::new);
        
        return validationToken.getIdentity();
    }

    public static String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }
        log.info("Extracting bearer token from authorization header: {}", authorizationHeader);
        return authorizationHeader.substring(7);
    }

    private boolean isTokenExpired(LocalDateTime expiresAt) {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
