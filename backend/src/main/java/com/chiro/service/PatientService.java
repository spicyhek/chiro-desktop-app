package com.chiro.service;

import com.chiro.dao.PatientDAO;
import com.chiro.models.Patient;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

// Service for Patient DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting. Call these methods in the controller
// Additional checks can be made if needed
@Service
public class PatientService {

    private final PatientDAO patientDAO;

    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.findAll();
    }

    public Patient getPatientById(String patientId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(patientId, "Patient ID");
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            throw new SQLException("Patient not found: " + patientId);
        }
        return patient;
    }

    public Patient savePatient(Patient patient) throws SQLException {
        ServiceValidationHelper.validateNotNull(patient, "Patient");

        if (patient.getPatientId() != null) {
            ServiceValidationHelper.validateNotBlank(patient.getPatientId(), "Patient ID");
        }

        ServiceValidationHelper.validateNotBlank(patient.getName(), "Patient name");

        if (patient.getEmail() != null) {
            ServiceValidationHelper.validateEmailFormat(patient.getEmail());
        }

        return patientDAO.save(patient);
    }

    public void deletePatient(String patientId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(patientId, "Patient ID");
        Patient existing = patientDAO.findById(patientId);
        if (existing == null) {
            throw new SQLException("Patient not found: " + patientId);
        }
        patientDAO.delete(patientId);
    }

}
