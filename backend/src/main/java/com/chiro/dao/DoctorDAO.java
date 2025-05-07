package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Doctor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoctorDAO {
    private final DatabaseConfig dbConfig;

    public DoctorDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public DoctorDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Doctor findById(String doctorId) throws SQLException {
        String sql = "SELECT * FROM Doctor WHERE doctorId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
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
        String sql = "SELECT * FROM Doctor";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        }
        return doctors;
    }

    public Doctor save(Doctor doctor) throws SQLException {
        if (doctor.getDoctorId() == null){
            return insert(doctor);
        } else {
            return update(doctor);
        }
    }

    private Doctor insert(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Doctor (doctorId, name, specialization, licenseNumber, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            doctor.setDoctorId(UUID.randomUUID().toString());
            setStatementParameters(stmt, doctor);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                doctor.setDoctorId(String.valueOf(keys.getInt(1))); // or keep as int if your model allow
            }
        }
        return doctor;
    }

    private Doctor update(Doctor doctor) throws SQLException {
        String sql = "UPDATE Doctor SET name = ?, specialization = ?, licenseNumber = ?, updatedAt = ? " +
                    "WHERE doctorId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getLicenseNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(5, doctor.getDoctorId());
            stmt.executeUpdate();
        }
        return doctor;
    }

    public void delete(String doctorId) throws SQLException {
        String sql = "DELETE FROM Doctor WHERE doctorId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctorId);
            stmt.executeUpdate();
        }
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getString("doctorId"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setLicenseNumber(rs.getString("licenseNumber"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        doctor.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        doctor.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return doctor;
    }

    private void setStatementParameters(PreparedStatement stmt, Doctor doctor) throws SQLException {
        stmt.setString(1, doctor.getDoctorId());
        stmt.setString(2, doctor.getName());
        stmt.setString(3, doctor.getSpecialization());
        stmt.setString(4, doctor.getLicenseNumber());
        stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
    }
} 