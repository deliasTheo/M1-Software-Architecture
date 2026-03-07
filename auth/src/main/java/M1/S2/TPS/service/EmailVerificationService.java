package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    
    /*
        sendVerificationEmail(...)

        verifyEmail(...)

        checkVerificationToken(...)

        generateVerificationToken

        verifyEmailToken

        markIdentityVerified

        publishUserRegisteredEvent
    */


        private TokenService tokenService;
        private EventPublisherService eventPublisherService;
}
