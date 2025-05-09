package com.chiro.service;

import com.chiro.dao.PatientDAO;
import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Patient;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Service for Patient DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting.
@Service
public class PatientService {

    private final PatientDAO patientDAO;
    private final InsuranceDAO insuranceDAO;

    public PatientService(PatientDAO patientDAO, InsuranceDAO insuranceDAO) {
        this.patientDAO = patientDAO;
        this.insuranceDAO = insuranceDAO;
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

        if (patient.getPhone() != null) {
            ServiceValidationHelper.validatePhoneNumberFormat(patient.getPhone());
        }

        if (patient.getInsuranceId() != null) {
            if (insuranceDAO.findById(patient.getInsuranceId()) == null) {
                throw new IllegalArgumentException("Insurance not found: " + patient.getInsuranceId());
            }
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

    public Patient updatePatientPartial(String id, Map<String, Object> updates) throws SQLException {
        Patient patient = getPatientById(id);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found for ID: " + id);
        }

        if (updates.containsKey("name")) {
            Object nameObj = updates.get("name");
            if (nameObj instanceof String name) {
                patient.setName(name);
            }
        }

        if (updates.containsKey("dateOfBirth")) {
            Object dobObj = updates.get("dateOfBirth");
            if (dobObj instanceof String dateOfBirth) {
                try {
                    patient.setDateOfBirth(LocalDate.parse(dateOfBirth));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format for dateOfBirth");
                }
            }
        }

        if (updates.containsKey("email")) {
            Object emailObj = updates.get("email");
            if (emailObj instanceof String email) {
                patient.setEmail(email);
            }
        }

        if (updates.containsKey("phone")) {
            Object phoneObj = updates.get("phone");
            if (phoneObj instanceof String phone) {
                patient.setPhone(phone);
            }
        }

        if (updates.containsKey("insuranceId")) {
            Object insuranceObj = updates.get("insuranceId");
            if (insuranceObj instanceof String insuranceId) {
                if (insuranceDAO.findById(insuranceId) == null) {
                    throw new IllegalArgumentException("Insurance not found: " + insuranceId);
                }
                patient.setInsuranceId(insuranceId);
            }
        }

        if (updates.containsKey("emergencyContactName")) {
            Object contactNameObj = updates.get("emergencyContactName");
            if (contactNameObj instanceof String contactName) {
                patient.setEmergencyContactName(contactName);
            }
        }

        if (updates.containsKey("emergencyContactPhone")) {
            Object contactPhoneObj = updates.get("emergencyContactPhone");
            if (contactPhoneObj instanceof String contactPhone) {
                patient.setEmergencyContactPhone(contactPhone);
            }
        }

        patientDAO.save(patient);
        return patient;
    }
}
