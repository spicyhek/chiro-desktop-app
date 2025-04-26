package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Doctor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    private final DatabaseConfig dbConfig;

    public DoctorDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public Doctor findById(int doctorId) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE DoctorId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDoctor(rs);
                }
            }
        }
        return null;
    }

    public List<Doctor> findAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        }
        return doctors;
    }

    public void save(Doctor doctor) throws SQLException {
        if (doctor.getDoctorId() == 0) {
            insert(doctor);
        } else {
            update(doctor);
        }
    }

    private void insert(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (Name, Specialization, LicenseNumber, CreatedAt, UpdatedAt) " +
                    "VALUES (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getLicenseNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(now));
            stmt.setTimestamp(5, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    doctor.setDoctorId(rs.getInt(1));
                }
            }
        }
    }

    private void update(Doctor doctor) throws SQLException {
        String sql = "UPDATE doctors SET Name = ?, Specialization = ?, LicenseNumber = ?, " +
                    "UpdatedAt = ? WHERE DoctorId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getLicenseNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(now));
            stmt.setInt(5, doctor.getDoctorId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int doctorId) throws SQLException {
        String sql = "DELETE FROM doctors WHERE DoctorId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, doctorId);
            stmt.executeUpdate();
        }
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("DoctorId"));
        doctor.setName(rs.getString("Name"));
        doctor.setSpecialization(rs.getString("Specialization"));
        doctor.setLicenseNumber(rs.getString("LicenseNumber"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        doctor.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        doctor.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return doctor;
    }
} 