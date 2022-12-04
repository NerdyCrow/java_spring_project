package by.slemnev.Clinic.service;

import by.slemnev.Clinic.model.Doctor;
import by.slemnev.Clinic.model.Patient;
import by.slemnev.Clinic.model.Reception;

import java.util.List;

public interface ReceptionService {
    Reception Add(Reception reception);
    List<Reception> getAll();
    List<Reception> getByIdDoctor(Doctor doctor);
    List<Reception> getByIdPatient(Patient patient);
    Reception findById(Long id);
    List<Reception> getByDate(String date);
    List<Reception> getByTime(String time);
    List<Reception> getByTimeAndDate(String time,String date);
    Boolean CheckDate(String date);
    void delete();
}
