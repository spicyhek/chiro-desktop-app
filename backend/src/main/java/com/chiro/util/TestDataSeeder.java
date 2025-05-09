package com.chiro.util;

import com.chiro.dao.DoctorDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Doctor;
import com.chiro.models.Patient;
import com.chiro.models.Insurance;
import java.time.LocalDateTime;


import java.sql.SQLException;
import java.time.LocalDate;

public class TestDataSeeder {
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;
    private final InsuranceDAO insuranceDAO;


    public TestDataSeeder(PatientDAO patientDAO, DoctorDAO doctorDAO, InsuranceDAO insuranceDAO) {
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
        this.insuranceDAO = insuranceDAO;
    }

    public void seedPatient(String id, String name) throws SQLException {
        if (patientDAO.findById(id) == null) {
            Patient patient = new Patient();
            patient.setPatientId(id);
            patient.setName(name);
            patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
            patient.setInsuranceId("test-insurance-1");
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
    
    public void seedInsurance(String id, String provider) throws SQLException {
        if (insuranceDAO.findById(id) == null) {
            Insurance insurance = new Insurance();
            insurance.setInsuranceId(id);
            insurance.setInsuranceProvider(provider);
            insurance.setCreatedAt(LocalDateTime.now());
            insurance.setUpdatedAt(LocalDateTime.now());
            insuranceDAO.save(insurance);
        }
    }

    public void seedDefaultTestData() throws SQLException {
        seedPatient("test-patient-1", "Test Patient");
        seedDoctor("test-doctor-1", "Test Doctor");
        seedInsurance("test-insurance-1", "Test Insurance Co");
    }
}
