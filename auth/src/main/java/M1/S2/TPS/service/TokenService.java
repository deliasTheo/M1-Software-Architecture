package M1.S2.TPS.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.AbstractToken;
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
    private final BCryptPasswordEncoder passwordEncoder;

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

    public void verifyToken(Identity identity, String token) {           
        List<AbstractToken> tokens = new ArrayList<>();
        for (SessionToken sessionToken : identity.getSessionTokens()) {
            tokens.add(sessionToken);
        }
        for (ValidationToken validationToken : identity.getValidationTokens()) {
            tokens.add(validationToken);
        }
// check la validité du token
        boolean isValid = tokens.stream().anyMatch(abstractToken -> 
            !isTokenExpired(abstractToken.getExpiresAt()) && 
            passwordEncoder.matches(token, abstractToken.getTokenHash())
        );
        
        if (!isValid) {
            throw new InvalidTokenException();
        }
    }

 
    private String hashToken(String token) {
        return passwordEncoder.encode(token);
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        log.info("Génération d'un token :");
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
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
