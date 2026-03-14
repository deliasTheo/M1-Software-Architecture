package M1.S2.TPS.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.ValidationToken;
import M1.S2.TPS.repository.ValidationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    
    private final ValidationTokenRepository validationTokenRepository;

    public ValidationToken createValidationToken(Identity identity) {
        String rawToken = generateToken();
        ValidationToken validationToken = new ValidationToken();
        validationToken.setIdentity(identity);
        validationToken.setTokenHash(rawToken);
        validationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        return validationToken;
    }

    public String getTokenValue(ValidationToken token) {
        return token.getTokenHash();
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
