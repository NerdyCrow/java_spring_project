package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> getAllById(Long id);
    List<Patient> getAllByUser(User user);
    List<Patient> findByUser(User user);

}
