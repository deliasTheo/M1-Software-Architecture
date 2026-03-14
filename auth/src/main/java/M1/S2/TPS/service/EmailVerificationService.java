package M1.S2.TPS.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.exception.EmailAlreadyVerifiedException;
import M1.S2.TPS.messaging.AuthEventPublisher;
import M1.S2.TPS.messaging.UserRegisteredEvent;
import M1.S2.TPS.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final TokenService tokenService;
    private final IdentityRepository identityRepository;
    private final AuthEventPublisher authEventPublisher;

    public void publishUserRegisteredEvent(Identity identity, String rawToken) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                "UserRegistered",
                UUID.randomUUID().toString(),
                OffsetDateTime.now().toString(),
                new UserRegisteredEvent.UserRegisteredData(
                        identity.getId(),
                        identity.getEmail(),
                        rawToken
                )
        );

        authEventPublisher.publishUserRegistered(event);
    }

    @Transactional
    public void validateEmail(String token) {
        Identity identity = tokenService.getIdentityByValidationToken(token);

        if (identity.isVerified()) {
            throw new EmailAlreadyVerifiedException();
        }

        tokenService.verifyTokenValidation(identity, token);
        identity.setVerified(true);
        identityRepository.save(identity);
    }

  
}
