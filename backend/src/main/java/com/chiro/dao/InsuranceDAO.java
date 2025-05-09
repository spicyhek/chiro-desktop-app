package com.chiro.dao;

import com.chiro.models.Insurance;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository // Indicates this class is a Spring-managed DAO component
public class InsuranceDAO {
    private final DataSource dataSource;

    // Constructor injection of the DataSource (provided by Spring)
    public InsuranceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper method to get a database connection
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Finds a single insurance record by its ID
    public Insurance findById(String insuranceId) throws SQLException {
        String sql = "SELECT * FROM Insurance WHERE insuranceId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, insuranceId); // Set the ID in the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Map the result row to an Insurance object
                    return mapResultSetToInsurance(rs);
                }
            }
        }
        return null; // Return null if not found
    }

    // Retrieves all insurance records
    public List<Insurance> findAll() throws SQLException {
        List<Insurance> insurances = new ArrayList<>();
        String sql = "SELECT * FROM Insurance";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Convert each row to an Insurance object and add to list
            while (rs.next()) {
                insurances.add(mapResultSetToInsurance(rs));
            }
        }
        return insurances;
    }

    // Saves an insurance record: inserts if not found, updates if it exists
    public void save(Insurance insurance) throws SQLException {
        // Defensive check: insuranceId must not be null or empty
        if (insurance.getInsuranceId() == null || insurance.getInsuranceId().isEmpty()) {
            throw new IllegalArgumentException("Insurance ID must be provided");
        }

        // Insert if new, otherwise update
        if (findById(insurance.getInsuranceId()) == null) {
            insert(insurance);
        } else {
            update(insurance);
        }
    }

    // Inserts a new insurance record into the database
    private void insert(Insurance insurance) throws SQLException {
        String sql = """
            INSERT INTO Insurance
              (insuranceId, insuranceProvider, createdAt, updatedAt)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setStatementParameters(stmt, insurance); // Fill in query placeholders
            stmt.executeUpdate();
        }
    }

    // Updates an existing insurance record
    private void update(Insurance insurance) throws SQLException {
        String sql = """
            UPDATE Insurance
            SET insuranceProvider = ?, updatedAt = ?
            WHERE insuranceId = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, insurance.getInsuranceProvider());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
            stmt.setString(3, insurance.getInsuranceId());

            stmt.executeUpdate();
        }
    }

    // Deletes an insurance record by ID
    public void delete(String insuranceId) throws SQLException {
        String sql = "DELETE FROM Insurance WHERE insuranceId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, insuranceId);
            stmt.executeUpdate();
        }
    }

    // Maps a ResultSet row to an Insurance object
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

    // Sets the values for an INSERT PreparedStatement from the Insurance object
    private void setStatementParameters(PreparedStatement stmt, Insurance insurance)
            throws SQLException {
        stmt.setString(1, insurance.getInsuranceId());
        stmt.setString(2, insurance.getInsuranceProvider());
        stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now())); // createdAt
        stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
    }
}
