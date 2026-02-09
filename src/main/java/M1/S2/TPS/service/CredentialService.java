package M1.S2.TPS.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import M1.S2.TPS.dto.CredentialDTO;
import M1.S2.TPS.entities.Credential;
import M1.S2.TPS.mapper.CredentialMapper;
import M1.S2.TPS.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CredentialService {
    
    @Autowired
    private CredentialRepository credentialRepository;
    @Autowired
    private CredentialMapper CredentialMapper;

    public CredentialDTO create(CredentialDTO credential) {
        Credential credentialEntity = CredentialMapper.toEntity(credential);
        if (credential.getValue() == null || credential.getValue().isEmpty()) {
            throw new IllegalArgumentException("Credential value cannot be null or empty");
        }
        return CredentialMapper.toDTO(credentialRepository.save(credentialEntity));
     }
    
    public Optional<CredentialDTO> getById(Long id) {
        return credentialRepository.findById(id).map(CredentialMapper::toDTO);
    }
    
    public List<CredentialDTO> getAll() {
        return credentialRepository.findAll().stream().map(CredentialMapper::toDTO).collect(Collectors.toList());
    }
    
    public CredentialDTO update(Long id, CredentialDTO credentialDetails) {
        Optional<Credential> credential = credentialRepository.findById(credentialDetails.getId());
        if (credential.isEmpty()) {
            throw new RuntimeException("Credential not found with id: " + id);
        }
        
        Credential existingCredential = credential.get();
        if (credentialDetails.getValue() != null) {
            existingCredential.setValue(credentialDetails.getValue());
        }
        return CredentialMapper.toDTO(credentialRepository.save(existingCredential));
    }
    
    public void delete(Long id) {
        if (!credentialRepository.existsById(id)) {
            throw new RuntimeException("Credential not found with id: " + id);
        }
        credentialRepository.deleteById(id);
    }
}
