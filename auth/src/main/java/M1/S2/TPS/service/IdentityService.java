package M1.S2.TPS.service;

import java.util.List;
import java.util.Optional;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour gérer la logique métier des identités.
 * Travaille uniquement avec les entités ; la conversion DTO est faite dans le controller.
 */
@Service
@Transactional
public class IdentityService {

    @Autowired
    private IdentityRepository identityRepository;

    public Identity create(Identity entity) {
        if (entity.getUid() == null || entity.getUid().isEmpty()) {
            throw new IllegalArgumentException("UID cannot be null or empty");
        }
        if (identityRepository.existsByUid(entity.getUid())) {
            throw new IllegalArgumentException("UID already exists: " + entity.getUid());
        }
        return identityRepository.save(entity);
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

    public Identity update(Identity entity) {
        Optional<Identity> existingOpt = identityRepository.findById(entity.getId());
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Identity not found with id: " + entity.getId());
        }

        Identity existing = existingOpt.get();
        if (entity.getUid() != null && !entity.getUid().equals(existing.getUid())) {
            if (identityRepository.existsByUid(entity.getUid())) {
                throw new IllegalArgumentException("UID already exists: " + entity.getUid());
            }
            existing.setUid(entity.getUid());
        }
        return identityRepository.save(existing);
    }

    public void delete(Long id) {
        if (!identityRepository.existsById(id)) {
            throw new RuntimeException("Identity not found with id: " + id);
        }
        identityRepository.deleteById(id);
    }
}
