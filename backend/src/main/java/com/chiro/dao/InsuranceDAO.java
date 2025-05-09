
package com.chiro.dao;

import com.chiro.models.Insurance;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class InsuranceDAO {
    private final DataSource dataSource;

    public InsuranceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Insurance findById(String insuranceId) throws SQLException {
        String sql = "SELECT * FROM Insurance WHERE insuranceId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insuranceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInsurance(rs);
                }
            }
        }
        return null;
    }

    public Insurance save(Insurance insurance) throws SQLException {
        String sql = "INSERT INTO Insurance (insuranceId, insuranceProvider) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String uuid = UUID.randomUUID().toString();
            insurance.setInsuranceId(uuid);
            stmt.setString(1, uuid);
            stmt.setString(2, insurance.getInsuranceProvider());
            stmt.executeUpdate();
        }
        return insurance;
    }

    public List<Insurance> findAll() throws SQLException {
        List<Insurance> insurances = new ArrayList<>();
        String sql = "SELECT * FROM Insurance";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                insurances.add(mapResultSetToInsurance(rs));
            }
        }
        return insurances;
    }

    public void delete(String insuranceId) throws SQLException {
        String sql = "DELETE FROM Insurance WHERE insuranceId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insuranceId);
            stmt.executeUpdate();
        }
    }
    public Insurance update(Insurance insurance) throws SQLException {
        String sql = """
      UPDATE Insurance
         SET insuranceProvider = ?,
             updatedAt = ?
       WHERE insuranceId = ?
      """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insurance.getInsuranceProvider());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, insurance.getInsuranceId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No insurance found with ID: " + insurance.getInsuranceId());
            }
        }
        return insurance;
    }

    private Insurance mapResultSetToInsurance(ResultSet rs) throws SQLException {
        Insurance insurance = new Insurance();
        insurance.setInsuranceId(rs.getString("insuranceId"));
        insurance.setInsuranceProvider(rs.getString("insuranceProvider"));
        Timestamp created = rs.getTimestamp("createdAt");
        if (created != null) {
            insurance.setCreatedAt(created.toLocalDateTime());
        }
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) {
            insurance.setUpdatedAt(updated.toLocalDateTime());
        }
        return insurance;
    }
}