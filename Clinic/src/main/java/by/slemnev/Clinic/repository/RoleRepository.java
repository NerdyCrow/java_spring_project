package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Role findRolesById(Long id);

}

