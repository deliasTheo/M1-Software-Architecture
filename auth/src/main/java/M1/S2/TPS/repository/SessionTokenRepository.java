package M1.S2.TPS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.SessionToken;
import M1.S2.TPS.entities.Identity;

public interface SessionTokenRepository extends JpaRepository<SessionToken, Integer> {

    Optional<SessionToken> findByTokenHash(String token);
    Optional<SessionToken> findByIdentity(Identity identity);
}
