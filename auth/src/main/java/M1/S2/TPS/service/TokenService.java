package M1.S2.TPS.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import M1.S2.TPS.dto.SessionTokenResult;
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

    // ==================== VALIDATION TOKENS (stockés en brut) ====================

    public ValidationToken createValidationToken(Identity identity) {
        String rawToken = generateToken();
        ValidationToken validationToken = new ValidationToken();
        validationToken.setIdentity(identity);
        validationToken.setTokenHash(rawToken);
        validationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        return validationToken;
    }

    public Identity getIdentityByValidationToken(String rawToken) {
        ValidationToken validationToken = validationTokenRepository.findByTokenHash(rawToken)
                .orElseThrow(InvalidTokenException::new);
        
        if (isTokenExpired(validationToken.getExpiresAt())) {
            throw new InvalidTokenException();
        }
        
        return validationToken.getIdentity();
    }

    public void verifyTokenValidation(Identity identity, String rawToken) {
        ValidationToken validationToken = validationTokenRepository.findByIdentity(identity)
                .orElseThrow(InvalidTokenException::new);

        if (!validationToken.getTokenHash().equals(rawToken)) {
            throw new InvalidTokenException();
        }
        if (isTokenExpired(validationToken.getExpiresAt())) {
            throw new InvalidTokenException();
        }
        if (validationToken.isUsed()) {
            throw new InvalidTokenException();
        }
        validationToken.setUsed(true);
        validationTokenRepository.save(validationToken);
    }

    // ==================== SESSION TOKENS (hashés avec BCrypt) ====================

    public SessionTokenResult createSessionToken(Identity identity) {
        String rawToken = generateToken();
        SessionToken sessionToken = new SessionToken();
        sessionToken.setIdentity(identity);
        sessionToken.setTokenHash(hashToken(rawToken));
        sessionToken.setExpiresAt(LocalDateTime.now().plusHours(4));
        return new SessionTokenResult(rawToken, sessionToken);
    }

    public Identity getIdentityBySessionToken(String rawToken) {
        List<SessionToken> allTokens = sessionTokenRepository.findAll();
        
        SessionToken matchingToken = allTokens.stream()
                .filter(st -> !isTokenExpired(st.getExpiresAt()))
                .filter(st -> passwordEncoder.matches(rawToken, st.getTokenHash()))
                .findFirst()
                .orElseThrow(InvalidTokenException::new);
               
        return matchingToken.getIdentity();
    }

    public void verifySessionToken(Identity identity, String rawToken) {           
        boolean isValid = identity.getSessionTokens().stream().anyMatch(sessionToken -> 
            !isTokenExpired(sessionToken.getExpiresAt()) && 
            passwordEncoder.matches(rawToken, sessionToken.getTokenHash())
        );
        
        if (!isValid) {
            throw new InvalidTokenException();
        }
    }

    public SessionToken getSessionToken(String rawToken) {
        List<SessionToken> allTokens = sessionTokenRepository.findAll();
        
        return allTokens.stream()
                .filter(st -> passwordEncoder.matches(rawToken, st.getTokenHash()))
                .findFirst()
                .orElseThrow(InvalidTokenException::new);
    }

    // ==================== UTILS ====================

    private String hashToken(String token) {
        return passwordEncoder.encode(token);
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public static String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }
        return authorizationHeader.substring(7);
    }

    private boolean isTokenExpired(LocalDateTime expiresAt) {
        return expiresAt.isBefore(LocalDateTime.now());
    }
}
