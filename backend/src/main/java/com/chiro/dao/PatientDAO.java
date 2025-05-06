package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Patient;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientDAO {
    private final DatabaseConfig dbConfig;

    public PatientDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public PatientDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Patient findById(String patientId) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE patientId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        }
        return null;
    }

    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    public Patient save(Patient patient) throws SQLException {
        if (patient.getPatientId() == null) {
            return insert(patient);
        } else {
            update(patient);
            return patient;
        }
    }

    private Patient insert(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patient (patientId, name, dateOfBirth, email, phone, " +
                    "emergencyContactName, emergencyContactPhone, createdAt, updatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            patient.setPatientId(UUID.randomUUID().toString());
            setStatementParameters(stmt, patient);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                patient.setPatientId(String.valueOf(keys.getInt(1))); // or keep as int if your model allows
            }
        }
        return patient;
    }

    private void update(Patient patient) throws SQLException {
        String sql = "UPDATE Patient SET name = ?, dateOfBirth = ?, email = ?, phone = ?, " +
                    "emergencyContactName = ?, emergencyContactPhone = ?, updatedAt = ? " +
                    "WHERE patientId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setDate(2, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getEmergencyContactName());
            stmt.setString(6, patient.getEmergencyContactPhone());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, patient.getPatientId());
            stmt.executeUpdate();
        }
    }

    public void delete(String patientId) throws SQLException {
        String sql = "DELETE FROM Patient WHERE patientId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            stmt.executeUpdate();
        }
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getString("PatientId"));
        patient.setName(rs.getString("Name"));
        
        Date dateOfBirth = rs.getDate("DateOfBirth");
        patient.setDateOfBirth(dateOfBirth != null ? dateOfBirth.toLocalDate() : null);
        
        patient.setEmail(rs.getString("Email"));
        patient.setPhone(rs.getString("Phone"));
        patient.setEmergencyContactName(rs.getString("EmergencyContactName"));
        patient.setEmergencyContactPhone(rs.getString("EmergencyContactPhone"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        patient.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        patient.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return patient;
    }

    private void setStatementParameters(PreparedStatement stmt, Patient patient) throws SQLException {
        stmt.setString(1, patient.getPatientId());
        stmt.setString(2, patient.getName());
        stmt.setDate(3, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
        stmt.setString(4, patient.getEmail());
        stmt.setString(5, patient.getPhone());
        stmt.setString(6, patient.getEmergencyContactName());
        stmt.setString(7, patient.getEmergencyContactPhone());
        stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
    }
} 