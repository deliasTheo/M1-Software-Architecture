package M1.S2.TPS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.ValidationToken;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Integer> {
    
}
