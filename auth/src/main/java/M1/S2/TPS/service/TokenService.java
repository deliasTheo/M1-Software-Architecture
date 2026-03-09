package M1.S2.TPS.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.ValidationToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    /*
        create token

        revoke token

        hash token
    */
    public ValidationToken createValidationToken(Identity identity) {
        ValidationToken validationToken = new ValidationToken();
        validationToken.setIdentity(identity);
        validationToken.setTokenHash(hashToken(generateToken()));
        validationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        return validationToken;
    }

    private String hashToken(String token) {
        return token;
    }

    private String generateToken() {
        return "token_hash";
    }
}
