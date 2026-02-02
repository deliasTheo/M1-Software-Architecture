package M1.S2.TPS.service;

import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CredentialService {
    
    @Autowired
    private CredentialRepository credentialRepository;
    
    public Credential create(Credential credential) {
        if (credential.getValue() == null || credential.getValue().isEmpty()) {
            throw new IllegalArgumentException("Credential value cannot be null or empty");
        }
        return credentialRepository.save(credential);
    }
    
    public Optional<Credential> getById(Long id) {
        return credentialRepository.findById(id);
    }
    
    public List<Credential> getAll() {
        return credentialRepository.findAll();
    }
    
    public Credential update(Long id, Credential credentialDetails) {
        Optional<Credential> credential = credentialRepository.findById(id);
        if (credential.isEmpty()) {
            throw new RuntimeException("Credential not found with id: " + id);
        }
        
        Credential existingCredential = credential.get();
        if (credentialDetails.getValue() != null) {
            existingCredential.setValue(credentialDetails.getValue());
        }
        
        return credentialRepository.save(existingCredential);
    }
    
    public void delete(Long id) {
        if (!credentialRepository.existsById(id)) {
            throw new RuntimeException("Credential not found with id: " + id);
        }
        credentialRepository.deleteById(id);
    }
}
