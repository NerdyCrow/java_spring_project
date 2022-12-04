package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.Patient;
import org.springframework.data.repository.CrudRepository;

public interface PatientCrudRepository extends CrudRepository<Patient,Long> {
}
