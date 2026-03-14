package M1.S2.TPS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.Identity;
import M1.S2.TPS.entities.ValidationToken;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Integer> {
    
    Optional<ValidationToken> findByTokenHash(String token);
    Optional<ValidationToken> findByIdentity(Identity identity);
    List<ValidationToken> findAllByIdentity(Identity identity);
}
