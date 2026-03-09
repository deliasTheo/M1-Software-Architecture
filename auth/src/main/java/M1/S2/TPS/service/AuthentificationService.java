package M1.S2.TPS.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import M1.S2.TPS.dto.IdentityDTO;
import M1.S2.TPS.entities.ValidationToken;
import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.repository.CredentialRepository;
import M1.S2.TPS.repository.IdentityRepository;
import M1.S2.TPS.repository.ValidationTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthentificationService {
    
     /*
        register(...)

        login(...)

        createSessionToken(...)

        checkPassword(...)
    */

    private final PasswordService passwordService;
    private final TokenService tokenService;
    private final EmailVerificationService emailVerificationService;
    private final IdentityRepository identityRepository;
    private final CredentialRepository credentialRepository;
    private final ValidationTokenRepository validationTokenRepository;
   
    @Transactional
    public void register(IdentityDTO identityDTO) {
        String hashedPassword = passwordService.hashPassword(identityDTO.getPassword());
       
        // create identity
        Identity identity = new Identity();
        identity.setEmail(identityDTO.getEmail());
        identity.setUid(UUID.randomUUID().toString());

        // save identity first to get the ID
        identityRepository.save(identity);

        // create credential with persisted identity
        Credential credential = new Credential();
        credential.setIdentity(identity);
        credential.setPasswordHash(hashedPassword);
        credentialRepository.save(credential);

        identity.setCredential(credential);

        // create and save validation token
        ValidationToken validationToken = tokenService.createValidationToken(identity);
        validationTokenRepository.save(validationToken);

        // publish user registered event
        emailVerificationService.publishUserRegisteredEvent(identity);


    }
        
    
}
