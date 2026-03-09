package M1.S2.TPS.service;

import org.springframework.stereotype.Service;

import M1.S2.TPS.entities.Identity;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final TokenService tokenService;

    // todo : pierre
    public void publishUserRegisteredEvent(Identity identity) {
        // publish l'evenement 
        // TODO: Implement this method
    }

    // todo : pierre
    public void validateEmail(String token) {
        Identity identity = tokenService.getIdentityByValidationToken(token);

        // si le token est valide, mettre la verification a true, sinon, generer des erreurs : 
        // 409  | Déjà validé

        tokenService.verifyToken(identity, token);
            
        
    }

  
}
