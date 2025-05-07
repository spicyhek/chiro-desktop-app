package com.chiro.dao;

import com.chiro.models.Insurance;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Insurance> findAll() throws SQLException {
        List<Insurance> insurances = new ArrayList<>();
        String sql = "SELECT * FROM Insurance";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                insurances.add(mapResultSetToInsurance(rs));
            }
        }
        return insurances;
    }

    public void save(Insurance insurance) throws SQLException {
        if (insurance.getInsuranceId() == null || insurance.getInsuranceId().isEmpty()) {
            throw new IllegalArgumentException("Insurance ID must be provided");
        }
        if (findById(insurance.getInsuranceId()) == null) {
            insert(insurance);
        } else {
            update(insurance);
        }
    }

    private void insert(Insurance insurance) throws SQLException {
        String sql = """
            INSERT INTO Insurance
              (insuranceId, insuranceProvider, createdAt, updatedAt)
            VALUES (?, ?, ?, ?)
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            setStatementParameters(stmt, insurance);
            stmt.executeUpdate();
        }
    }

    private void update(Insurance insurance) throws SQLException {
        String sql = """
            UPDATE Insurance
            SET insuranceProvider = ?, updatedAt = ?
            WHERE insuranceId = ?
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insurance.getInsuranceProvider());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(3, insurance.getInsuranceId());
            stmt.executeUpdate();
        }
    }

    public void delete(String insuranceId) throws SQLException {
        String sql = "DELETE FROM Insurance WHERE insuranceId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insuranceId);
            stmt.executeUpdate();
        }
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

    private void setStatementParameters(PreparedStatement stmt, Insurance insurance)
            throws SQLException {
        stmt.setString(1, insurance.getInsuranceId());
        stmt.setString(2, insurance.getInsuranceProvider());
        stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
    }
}
