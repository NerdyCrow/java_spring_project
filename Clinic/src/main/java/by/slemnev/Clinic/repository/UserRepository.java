package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
   Optional<User> findById(Long id);
    List<User> findByFirstNameAndLastName(String first,String last);
    Page<User> findAll(Pageable pageable);
}

