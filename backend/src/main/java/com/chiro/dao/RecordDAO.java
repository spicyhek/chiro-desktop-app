package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Record;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordDAO {
    private final DatabaseConfig dbConfig;

    public RecordDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public RecordDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Record findById(String recordId) throws SQLException {
        String sql = "SELECT * FROM records WHERE RecordId = ?";
        try (Connection conn = dbConfig.getConnection();
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

    public List<Record> findAll() throws SQLException {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM records";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        }
        return records;
    }

    public void save(Record record) throws SQLException {
        if (record.getRecordId() == null) {
            insert(record);
        } else {
            update(record);
        }
    }

    private void insert(Record record) throws SQLException {
        String sql = "INSERT INTO records (RecordId, AppointmentId, VisitDate, Symptoms, Diagnosis, " +
                    "NextVisitRecommendedDate, RecordNotes, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String recordId = UUID.randomUUID().toString();
            record.setRecordId(recordId);
            
            stmt.setString(1, recordId);
            stmt.setString(2, record.getAppointmentId());
            stmt.setDate(3, record.getVisitDate() != null ? Date.valueOf(record.getVisitDate()) : null);
            stmt.setString(4, record.getSymptoms());
            stmt.setString(5, record.getDiagnosis());
            stmt.setDate(6, record.getNextVisitRecommendedDate() != null ? 
                        Date.valueOf(record.getNextVisitRecommendedDate()) : null);
            stmt.setString(7, record.getRecordNotes());
            stmt.setTimestamp(8, Timestamp.valueOf(now));
            stmt.setTimestamp(9, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
        }
    }

    private void update(Record record) throws SQLException {
        String sql = "UPDATE records SET AppointmentId = ?, VisitDate = ?, Symptoms = ?, " +
                    "Diagnosis = ?, NextVisitRecommendedDate = ?, RecordNotes = ?, " +
                    "UpdatedAt = ? WHERE RecordId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, record.getAppointmentId());
            stmt.setDate(2, record.getVisitDate() != null ? Date.valueOf(record.getVisitDate()) : null);
            stmt.setString(3, record.getSymptoms());
            stmt.setString(4, record.getDiagnosis());
            stmt.setDate(5, record.getNextVisitRecommendedDate() != null ? 
                        Date.valueOf(record.getNextVisitRecommendedDate()) : null);
            stmt.setString(6, record.getRecordNotes());
            stmt.setTimestamp(7, Timestamp.valueOf(now));
            stmt.setString(8, record.getRecordId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(String recordId) throws SQLException {
        String sql = "DELETE FROM records WHERE RecordId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recordId);
            stmt.executeUpdate();
        }
    }

    private Record mapResultSetToRecord(ResultSet rs) throws SQLException {
        Record record = new Record();
        record.setRecordId(rs.getString("RecordId"));
        record.setAppointmentId(rs.getString("AppointmentId"));
        
        Date visitDate = rs.getDate("VisitDate");
        record.setVisitDate(visitDate != null ? visitDate.toLocalDate() : null);
        
        record.setSymptoms(rs.getString("Symptoms"));
        record.setDiagnosis(rs.getString("Diagnosis"));
        
        Date nextVisitDate = rs.getDate("NextVisitRecommendedDate");
        record.setNextVisitRecommendedDate(nextVisitDate != null ? nextVisitDate.toLocalDate() : null);
        
        record.setRecordNotes(rs.getString("RecordNotes"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        record.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        record.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return record;
    }
} 