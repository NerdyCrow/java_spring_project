package by.slemnev.Clinic.service;

import by.slemnev.Clinic.model.Doctor;
import by.slemnev.Clinic.model.User;

import java.util.List;

public interface DoctorService {
    Doctor register(Doctor doctor);

    List<Doctor> getAll();
    Boolean GetUserInDoctor(User user);
    Doctor findById(Long id);
    Long findbyUser(User user);
    Long getRoleForId(Long id);
    void delete(Long id);
    void Update(Doctor doctor);
}
