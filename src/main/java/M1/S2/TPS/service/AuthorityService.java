package M1.S2.TPS.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import M1.S2.TPS.dto.AuthorityDTO;
import M1.S2.TPS.entities.Authority;
import M1.S2.TPS.mapper.AuthorityMapper;
import M1.S2.TPS.repository.AuthorityRepository;

@Service
@Transactional
public class AuthorityService {
    
    @Autowired
    private AuthorityRepository authorityRepository;
    
    @Autowired
    private AuthorityMapper authorityMapper;
    
    public AuthorityDTO create(AuthorityDTO authority) {
        Authority authorityEntity = authorityMapper.toEntity(authority);
        if (authorityEntity.getName() == null || authorityEntity.getName().isEmpty()) {
            throw new IllegalArgumentException("Authority name cannot be null or empty");
        }
        return authorityMapper.toDTO(authorityRepository.save(authorityEntity));
    }
    
    public Optional<AuthorityDTO> getById(Long id) {
        return authorityRepository.findById(id).map(authorityMapper::toDTO);
    }
    
    public Optional<AuthorityDTO> getByName(String name) {
        return authorityRepository.findByName(name).map(authorityMapper::toDTO);
    }
    
    public List<AuthorityDTO> getAll() {
        return authorityRepository.findAll().stream().map(authorityMapper::toDTO).collect(Collectors.toList());
    }
    
    public AuthorityDTO update(AuthorityDTO authorityDetails) {
        
        Optional<Authority> authority = authorityRepository.findById(authorityDetails.getId());


        if (authority.isEmpty()) {
            throw new RuntimeException("Authority not found with id: " + authorityDetails.getId());
        }
        
        Authority existingAuthority = authority.get();
        if (authorityDetails.getName() != null) {
            existingAuthority.setName(authorityDetails.getName());
        }
        if (authorityDetails.getDescription() != null) {
            existingAuthority.setDescription(authorityDetails.getDescription());
        }
        
        AuthorityDTO autority =  authorityMapper.toDTO(authorityRepository.save(existingAuthority));
        return autority;
    }
    
    public void delete(Long id) {
        if (!authorityRepository.existsById(id)) {
            throw new RuntimeException("Authority not found with id: " + id);
        }
        authorityRepository.deleteById(id);
    }
}
