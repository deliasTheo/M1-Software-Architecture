package M1.S2.TPS.repository;

import M1.S2.TPS.entities.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, Long> {
    Optional<Identity> findByUid(String uid);
    boolean existsByUid(String uid);
}
