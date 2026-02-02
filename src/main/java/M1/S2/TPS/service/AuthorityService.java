package M1.S2.TPS.service;

import M1.S2.TPS.entities.Authority;
import M1.S2.TPS.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorityService {
    
    @Autowired
    private AuthorityRepository authorityRepository;
    
    public Authority create(Authority authority) {
        if (authority.getName() == null || authority.getName().isEmpty()) {
            throw new IllegalArgumentException("Authority name cannot be null or empty");
        }
        return authorityRepository.save(authority);
    }
    
    public Optional<Authority> getById(Long id) {
        return authorityRepository.findById(id);
    }
    
    public Optional<Authority> getByName(String name) {
        return authorityRepository.findByName(name);
    }
    
    public List<Authority> getAll() {
        return authorityRepository.findAll();
    }
    
    public Authority update(Long id, Authority authorityDetails) {
        Optional<Authority> authority = authorityRepository.findById(id);
        if (authority.isEmpty()) {
            throw new RuntimeException("Authority not found with id: " + id);
        }
        
        Authority existingAuthority = authority.get();
        if (authorityDetails.getName() != null) {
            existingAuthority.setName(authorityDetails.getName());
        }
        if (authorityDetails.getDescription() != null) {
            existingAuthority.setDescription(authorityDetails.getDescription());
        }
        
        return authorityRepository.save(existingAuthority);
    }
    
    public void delete(Long id) {
        if (!authorityRepository.existsById(id)) {
            throw new RuntimeException("Authority not found with id: " + id);
        }
        authorityRepository.deleteById(id);
    }
}
