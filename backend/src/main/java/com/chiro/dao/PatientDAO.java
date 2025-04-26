package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Patient;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private final DatabaseConfig dbConfig;

    public PatientDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public Patient findById(int patientId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE PatientId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
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
        String sql = "SELECT * FROM patients";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    public void save(Patient patient) throws SQLException {
        if (patient.getPatientId() == 0) {
            insert(patient);
        } else {
            update(patient);
        }
    }

    private void insert(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (Name, DateOfBirth, Email, Phone, EmergencyContactName, " +
                    "EmergencyContactPhone, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getName());
            stmt.setDate(2, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getEmergencyContactName());
            stmt.setString(6, patient.getEmergencyContactPhone());
            stmt.setTimestamp(7, Timestamp.valueOf(now));
            stmt.setTimestamp(8, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setPatientId(rs.getInt(1));
                }
            }
        }
    }

    private void update(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET Name = ?, DateOfBirth = ?, Email = ?, Phone = ?, " +
                    "EmergencyContactName = ?, EmergencyContactPhone = ?, UpdatedAt = ? " +
                    "WHERE PatientId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getName());
            stmt.setDate(2, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getEmergencyContactName());
            stmt.setString(6, patient.getEmergencyContactPhone());
            stmt.setTimestamp(7, Timestamp.valueOf(now));
            stmt.setInt(8, patient.getPatientId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int patientId) throws SQLException {
        String sql = "DELETE FROM patients WHERE PatientId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.executeUpdate();
        }
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getInt("PatientId"));
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
} 