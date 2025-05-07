package com.chiro.dao;

import com.chiro.models.MedicalRecord;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RecordDAO {
    private final DataSource dataSource;

    public RecordDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public MedicalRecord findById(String recordId) throws SQLException {
        String sql = "SELECT * FROM Record WHERE recordId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRecord(rs);
                }
            }
        }
        return null;
    }

    public List<MedicalRecord> findAll() throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM Record";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        }
        return records;
    }

    public void save(MedicalRecord record) throws SQLException {
        if (record.getRecordId() == null || record.getRecordId().isEmpty()) {
            insert(record);
        } else {
            update(record);
        }
    }

    private void insert(MedicalRecord record) throws SQLException {
        String sql = """
            INSERT INTO Record (
              recordId, patientId, doctorId, appointmentId,
              visitDate, symptoms, diagnosis, treatment,
              notes, nextVisitRecommendedDate, createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            record.setRecordId(UUID.randomUUID().toString());
            setStatementParameters(stmt, record);
            stmt.executeUpdate();
        }
    }

    private void update(MedicalRecord record) throws SQLException {
        String sql = """
            UPDATE Record SET
              patientId = ?, doctorId = ?, appointmentId = ?,
              visitDate = ?, symptoms = ?, diagnosis = ?,
              treatment = ?, notes = ?, nextVisitRecommendedDate = ?,
              updatedAt = ?
            WHERE recordId = ?
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getPatientId());
            stmt.setString(2, record.getDoctorId());
            stmt.setString(3, record.getAppointmentId());
            stmt.setDate(4, record.getVisitDate() != null
                    ? Date.valueOf(record.getVisitDate())
                    : null);
            stmt.setString(5, record.getSymptoms());
            stmt.setString(6, record.getDiagnosis());
            stmt.setString(7, record.getTreatment());
            stmt.setString(8, record.getNotes());
            stmt.setDate(9, record.getNextVisitRecommendedDate() != null
                    ? Date.valueOf(record.getNextVisitRecommendedDate())
                    : null);
            stmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(11, record.getRecordId());
            stmt.executeUpdate();
        }
    }

    public void delete(String recordId) throws SQLException {
        String sql = "DELETE FROM Record WHERE recordId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recordId);
            stmt.executeUpdate();
        }
    }

    private MedicalRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        MedicalRecord record = new MedicalRecord();
        record.setRecordId(rs.getString("recordId"));
        record.setPatientId(rs.getString("patientId"));
        record.setDoctorId(rs.getString("doctorId"));
        record.setAppointmentId(rs.getString("appointmentId"));
        record.setVisitDate(rs.getDate("visitDate") != null
                ? rs.getDate("visitDate").toLocalDate()
                : null);
        record.setSymptoms(rs.getString("symptoms"));
        record.setDiagnosis(rs.getString("diagnosis"));
        record.setTreatment(rs.getString("treatment"));
        record.setNotes(rs.getString("notes"));
        record.setNextVisitRecommendedDate(rs.getDate("nextVisitRecommendedDate") != null
                ? rs.getDate("nextVisitRecommendedDate").toLocalDate()
                : null);

        Timestamp created = rs.getTimestamp("createdAt");
        if (created != null) {
            record.setCreatedAt(created.toLocalDateTime());
        }
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) {
            record.setUpdatedAt(updated.toLocalDateTime());
        }
        return record;
    }

    private void setStatementParameters(PreparedStatement stmt, MedicalRecord record)
            throws SQLException {
        stmt.setString(1, record.getRecordId());
        stmt.setString(2, record.getPatientId());
        stmt.setString(3, record.getDoctorId());
        stmt.setString(4, record.getAppointmentId());
        stmt.setDate(5, record.getVisitDate() != null
                ? Date.valueOf(record.getVisitDate())
                : null);
        stmt.setString(6, record.getSymptoms());
        stmt.setString(7, record.getDiagnosis());
        stmt.setString(8, record.getTreatment());
        stmt.setString(9, record.getNotes());
        stmt.setDate(10, record.getNextVisitRecommendedDate() != null
                ? Date.valueOf(record.getNextVisitRecommendedDate())
                : null);
        stmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
    }
}
