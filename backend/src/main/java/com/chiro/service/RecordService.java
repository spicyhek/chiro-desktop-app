package com.chiro.service;

import com.chiro.dao.RecordDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.MedicalRecord;
import com.chiro.models.Patient;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public List<MedicalRecord> searchRecords(String nameFilter) {
        try {
            return recordDAO.searchByName(nameFilter);
        } catch (SQLException ex) {
            throw new RuntimeException("Error searching patients", ex);
        }
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

    public MedicalRecord updateRecordPartial(String id, Map<String, Object> updates) throws SQLException {
        MedicalRecord record = getRecordById(id);
        if (record == null) {
            throw new IllegalArgumentException("Record not found for ID: " + id);
        }

        // Safely update visitDate
        Object dateObj = updates.get("visitDate");
        if (dateObj instanceof String dateStr && !dateStr.isBlank()) {
            try {
                record.setVisitDate(LocalDate.parse(dateStr));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format for visitDate");
            }
        }

        // Safely update symptoms
        Object symptomsObj = updates.get("symptoms");
        if (symptomsObj instanceof String symptoms && !symptoms.isBlank()) {
            record.setSymptoms(symptoms);
        }

        // Safely update diagnosis
        Object diagnosisObj = updates.get("diagnosis");
        if (diagnosisObj instanceof String diagnosis && !diagnosis.isBlank()) {
            record.setDiagnosis(diagnosis);
        }
        Object treatmentObj = updates.get("treatment");
        if (treatmentObj instanceof String treatment && !treatment.isBlank()) {
            record.setTreatment(treatment);
        }

        // Safely update notes
        Object notesObj = updates.get("notes");
        if (notesObj instanceof String notes && !notes.isBlank()) {
            record.setNotes(notes);
        }

        // Safely update nextRecommendedVisit
        Object nextVisitObj = updates.get("nextRecommendedVisit");
        if (nextVisitObj instanceof String nextVisit && !nextVisit.isBlank()) {
            try {
                record.setNextVisitRecommendedDate(LocalDate.parse(nextVisit));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid date format for nextRecommendedVisit");
            }
        }
        // Save and return the updated record
        recordDAO.save(record);
        return record;
    }

}
