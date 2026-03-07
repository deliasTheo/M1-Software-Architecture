package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    
    /*
        validateAccess(...)

        hasAccessToService(...)

        isTokenValid(...)
    */
    private TokenService tokenService;
}
