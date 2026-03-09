package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
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


    private final TokenService tokenService;

    public void publishUserRegisteredEvent(Identity identity) {
        // TODO: Implement this method
    }

  
}
