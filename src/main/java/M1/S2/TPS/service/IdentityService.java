package M1.S2.TPS.service;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IdentityService {
    
    @Autowired
    private IdentityRepository identityRepository;
    
    public Identity create(Identity identity) {
        if (identity.getUid() == null || identity.getUid().isEmpty()) {
            throw new IllegalArgumentException("UID cannot be null or empty");
        }
        return identityRepository.save(identity);
    }
    
    public Optional<Identity> getById(Long id) {
        return identityRepository.findById(id);
    }
    
    public Optional<Identity> getByUid(String uid) {
        return identityRepository.findByUid(uid);
    }
    
    public List<Identity> getAll() {
        return identityRepository.findAll();
    }
    
    public Identity update(Long id, Identity identityDetails) {
        Optional<Identity> identity = identityRepository.findById(id);
        if (identity.isEmpty()) {
            throw new RuntimeException("Identity not found with id: " + id);
        }
        
        Identity existingIdentity = identity.get();
        if (identityDetails.getUid() != null) {
            existingIdentity.setUid(identityDetails.getUid());
        }
        
        return identityRepository.save(existingIdentity);
    }
    
    public void delete(Long id) {
        if (!identityRepository.existsById(id)) {
            throw new RuntimeException("Identity not found with id: " + id);
        }
        identityRepository.deleteById(id);
    }
}
