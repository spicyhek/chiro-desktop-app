package com.chiro.dao;

import com.chiro.models.Doctor;
import com.chiro.models.Patient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository // Marks this class as a Spring Bean, used for database access logic
public class DoctorDAO {
    private final DataSource dataSource;

    // Constructor injection of the DataSource, managed by Spring Boot
    public DoctorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper method to obtain a database connection
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Doctor> searchByName(String nameFilter) throws SQLException {
        String sql = "SELECT * FROM Doctor WHERE LOWER(name) LIKE ?";
        List<Doctor> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nameFilter.toLowerCase() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapResultSetToDoctor(rs));
                }
            }
        }

        return results;
    }


    // Retrieves a Doctor by its ID
    public Doctor findById(String doctorId) throws SQLException {
        String sql = "SELECT * FROM Doctor WHERE doctorId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId); // Set the ID parameter
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDoctor(rs); // Convert result to Doctor object
                }
            }
        }
        return null; // Return null if no doctor found
    }

    // Returns all doctors in the database
    public List<Doctor> findAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctor";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Add each row to the list of doctors
            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }
        }
        return doctors;
    }

    // Saves a doctor: inserts a new record or updates an existing one
    public Doctor save(Doctor doctor) throws SQLException {
        if (doctor.getDoctorId() == null) {
            return insert(doctor); // Insert if no ID
        } else {
            return update(doctor); // Update otherwise
        }
    }

    // Inserts a new doctor into the database
    private Doctor insert(Doctor doctor) throws SQLException {
        String sql = """
            INSERT INTO Doctor (
              doctorId, name, specialization, licenseNumber,
              createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generate a UUID as the doctor's unique ID
            doctor.setDoctorId(UUID.randomUUID().toString());

            // Set all parameters for the insert statement
            setStatementParameters(stmt, doctor);
            stmt.executeUpdate();
        }
        return doctor;
    }

    // Updates an existing doctor’s information
    private Doctor update(Doctor doctor) throws SQLException {
        String sql = """
            UPDATE Doctor
            SET name = ?, specialization = ?, licenseNumber = ?, updatedAt = ?
            WHERE doctorId = ?
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set updated field values
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getLicenseNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
            stmt.setString(5, doctor.getDoctorId());

            stmt.executeUpdate();
        }
        return doctor;
    }

    // Deletes a doctor from the database by ID
    public void delete(String doctorId) throws SQLException {
        String sql = "DELETE FROM Doctor WHERE doctorId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doctorId);
            stmt.executeUpdate();
        }
    }

    // Converts a single row in a ResultSet into a Doctor object
    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getString("doctorId"));
        doctor.setName(rs.getString("name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setLicenseNumber(rs.getString("licenseNumber"));

        // Handle nullable createdAt
        Timestamp c = rs.getTimestamp("createdAt");
        if (c != null) {
            doctor.setCreatedAt(c.toLocalDateTime());
        }

        // Handle nullable updatedAt
        Timestamp u = rs.getTimestamp("updatedAt");
        if (u != null) {
            doctor.setUpdatedAt(u.toLocalDateTime());
        }

        return doctor;
    }

    // Sets all parameters for SQL statements
    private void setStatementParameters(PreparedStatement stmt, Doctor doctor)
            throws SQLException {
        stmt.setString(1, doctor.getDoctorId());
        stmt.setString(2, doctor.getName());
        stmt.setString(3, doctor.getSpecialization());
        stmt.setString(4, doctor.getLicenseNumber());
        stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now())); // createdAt
        stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
    }
}
