package com.chiro.service;

import com.chiro.dao.RecordDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.MedicalRecord;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Service for Record DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting. Call these methods in the controller
// Additional checks can be made if needed
@Service
public class RecordService {

    private final RecordDAO recordDAO;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public RecordService(RecordDAO recordDAO, PatientDAO patientDAO, DoctorDAO doctorDAO) {
        this.recordDAO = recordDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
    }

    public List<MedicalRecord> getAllRecords() throws SQLException {
        return recordDAO.findAll();
    }

    public MedicalRecord getRecordById(String recordId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(recordId, "Record ID");
        MedicalRecord medicalRecord = recordDAO.findById(recordId);
        if (medicalRecord == null) {
            throw new SQLException("Record not found: " + recordId);
        }
        return medicalRecord;
    }

    public MedicalRecord saveRecord(MedicalRecord medicalRecord) throws SQLException {
        ServiceValidationHelper.validateNotNull(medicalRecord, "Record");

        if (medicalRecord.getRecordId() != null) {
            ServiceValidationHelper.validateNotBlank(medicalRecord.getRecordId(), "Record ID");
        }

        ServiceValidationHelper.validateNotBlank(medicalRecord.getPatientId(), "Patient ID");
        ServiceValidationHelper.validateNotBlank(medicalRecord.getDoctorId(), "Doctor ID");
        ServiceValidationHelper.validateNotBlank(medicalRecord.getDiagnosis(), "Diagnosis");

        if (medicalRecord.getVisitDate() != null && medicalRecord.getVisitDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Visit date cannot be in the future.");
        }

        if (patientDAO.findById(medicalRecord.getPatientId()) == null) {
            throw new IllegalArgumentException("Patient not found: " + medicalRecord.getPatientId());
        }

        if (doctorDAO.findById(medicalRecord.getDoctorId()) == null) {
            throw new IllegalArgumentException("Doctor not found: " + medicalRecord.getDoctorId());
        }

        recordDAO.save(medicalRecord);
        return medicalRecord;
    }

    public void deleteRecord(String recordId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(recordId, "Record ID");

        MedicalRecord existing = recordDAO.findById(recordId);
        if (existing == null) {
            throw new SQLException("Record not found: " + recordId);
        }

        recordDAO.delete(recordId);
    }
}
