package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.Doctor;
import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.Reception;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceptionRepository  extends JpaRepository<Reception, Long> {
   List<Reception> findByDoctor(Doctor doctor);
   List<Reception> findByPatient(Patient patient);
   List<Reception> findByPatientAndDoctor(Patient patient,Doctor doctor);
}
