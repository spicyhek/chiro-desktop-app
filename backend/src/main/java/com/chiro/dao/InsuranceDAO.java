package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Insurance;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InsuranceDAO {
    private final DatabaseConfig dbConfig;

    public InsuranceDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public InsuranceDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Insurance findById(int insuranceId) throws SQLException {
        String sql = "SELECT * FROM insurance WHERE InsuranceId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, insuranceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInsurance(rs);
                }
            }
        }
        return null;
    }

    public List<Insurance> findAll() throws SQLException {
        List<Insurance> insuranceList = new ArrayList<>();
        String sql = "SELECT * FROM insurance";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                insuranceList.add(mapResultSetToInsurance(rs));
            }
        }
        return insuranceList;
    }

    public void save(Insurance insurance) throws SQLException {
        if (insurance.getInsuranceId() == 0) {
            insert(insurance);
        } else {
            update(insurance);
        }
    }

    private void insert(Insurance insurance) throws SQLException {
        String sql = "INSERT INTO insurance (InsuranceProvider, CreatedAt, UpdatedAt) VALUES (?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, insurance.getInsuranceProvider());
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setTimestamp(3, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    insurance.setInsuranceId(rs.getInt(1));
                }
            }
        }
    }

    private void update(Insurance insurance) throws SQLException {
        String sql = "UPDATE insurance SET InsuranceProvider = ?, UpdatedAt = ? WHERE InsuranceId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insurance.getInsuranceProvider());
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setInt(3, insurance.getInsuranceId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int insuranceId) throws SQLException {
        String sql = "DELETE FROM insurance WHERE InsuranceId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, insuranceId);
            stmt.executeUpdate();
        }
    }

    private Insurance mapResultSetToInsurance(ResultSet rs) throws SQLException {
        Insurance insurance = new Insurance();
        insurance.setInsuranceId(rs.getInt("InsuranceId"));
        insurance.setInsuranceProvider(rs.getString("InsuranceProvider"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        insurance.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        insurance.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return insurance;
    }
} 