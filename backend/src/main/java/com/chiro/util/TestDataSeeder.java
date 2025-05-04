package com.chiro.util;

import com.chiro.dao.DoctorDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.models.Doctor;
import com.chiro.models.Patient;

import java.sql.SQLException;
import java.time.LocalDate;

public class TestDataSeeder {
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public TestDataSeeder(PatientDAO patientDAO, DoctorDAO doctorDAO) {
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
    }

    public void seedPatient(String id, String name) throws SQLException {
        if (patientDAO.findById(id) == null) {
            Patient patient = new Patient();
            patient.setPatientId(id);
            patient.setName(name);
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patientDAO.save(patient);
        }
    }

    public void seedDoctor(String id, String name) throws SQLException {
        if (doctorDAO.findById(id) == null) {
            Doctor doctor = new Doctor();
            doctor.setDoctorId(id);
            doctor.setName(name);
            doctor.setSpecialization("General");
            doctor.setLicenseNumber("D123");
            doctorDAO.save(doctor);
        }
    }

    public void seedDefaultTestData() throws SQLException {
        seedPatient("test-patient-1", "Test Patient");
        seedDoctor("test-doctor-1", "Test Doctor");
    }
}
