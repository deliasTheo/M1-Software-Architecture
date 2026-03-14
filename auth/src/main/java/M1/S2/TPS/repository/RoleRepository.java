package M1.S2.TPS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import M1.S2.TPS.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
