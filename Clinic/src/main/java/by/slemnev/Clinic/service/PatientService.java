package by.slemnev.Clinic.service;

import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.User;

import java.util.List;

public interface PatientService  {
    Patient register(Patient patient);

    List<Patient> getAll();
    Boolean GetUserInPatient(User user);
    Patient findById(Long id);
    Long getRoleForId(Long id);
    void delete(Long id);
    void Update(Patient patient);
    Long findbyUser(User user);
}