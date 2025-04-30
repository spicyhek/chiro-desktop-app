package com.chiro.service;

import com.chiro.dao.RecordDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.Record;
import com.chiro.util.ServiceValidationHelper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// Service for Record DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting. Call these methods in the controller
// Additional checks can be made if needed
public class RecordService {

    private final RecordDAO recordDAO;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public RecordService() {
        this.recordDAO = new RecordDAO();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
    }

    public RecordService(RecordDAO recordDAO, PatientDAO patientDAO, DoctorDAO doctorDAO) {
        this.recordDAO = recordDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
    }

    public List<Record> getAllRecords() throws SQLException {
        return recordDAO.findAll();
    }

    public Record getRecordById(String recordId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(recordId, "Record ID");
        Record record = recordDAO.findById(recordId);
        if (record == null) {
            throw new SQLException("Record not found: " + recordId);
        }
        return record;
    }

    public void saveRecord(Record record) throws SQLException {
        ServiceValidationHelper.validateNotNull(record, "Record");

        if (record.getRecordId() != null) {
            ServiceValidationHelper.validateNotBlank(record.getRecordId(), "Record ID");
        }

        ServiceValidationHelper.validateNotBlank(record.getPatientId(), "Patient ID");
        ServiceValidationHelper.validateNotBlank(record.getDoctorId(), "Doctor ID");
        ServiceValidationHelper.validateNotBlank(record.getDiagnosis(), "Diagnosis");

        if (record.getVisitDate() != null && record.getVisitDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Visit date cannot be in the future.");
        }

        if (patientDAO.findById(record.getPatientId()) == null) {
            throw new IllegalArgumentException("Patient not found: " + record.getPatientId());
        }

        if (doctorDAO.findById(record.getDoctorId()) == null) {
            throw new IllegalArgumentException("Doctor not found: " + record.getDoctorId());
        }

        recordDAO.save(record);
    }

    public void deleteRecord(String recordId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(recordId, "Record ID");

        Record existing = recordDAO.findById(recordId);
        if (existing == null) {
            throw new SQLException("Record not found: " + recordId);
        }

        recordDAO.delete(recordId);
    }
}
