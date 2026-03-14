package M1.S2.TPS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.Identity;

public interface IdentityRepository extends JpaRepository<Identity, Integer> {
    boolean existsByEmail(String email);
    Optional<Identity> findByEmail(String email);
}
