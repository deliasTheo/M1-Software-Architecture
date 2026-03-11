package M1.S2.TPS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    boolean existsByCode(String code);
    Optional<Authority> findByCode(String code);
}
