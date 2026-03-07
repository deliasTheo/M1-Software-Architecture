package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    
    private PasswordService passwordService;
    private TokenService tokenService;
    /*
        register(...)

        login(...)

        createSessionToken(...)

        checkPassword(...)
    */
    
}
