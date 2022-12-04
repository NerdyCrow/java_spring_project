package by.slemnev.Clinic.repository;

import by.slemnev.Clinic.model.Doctor;
import org.springframework.data.repository.CrudRepository;

public interface DoctorCrudRepository  extends CrudRepository<Doctor,Long> {
}
