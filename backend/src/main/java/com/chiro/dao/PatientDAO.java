package com.chiro.dao;

import com.chiro.models.Patient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository // Marks this as a Spring-managed DAO component
public class PatientDAO {
    private final DataSource dataSource;

    // Constructor injection of the DataSource (configured by Spring)
    public PatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper method to obtain a JDBC connection from the DataSource
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Finds a single patient by ID
    public Patient findById(String patientId) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId); // Bind ID value
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs); // Convert result to Patient
                }
            }
        }
        return null; // Return null if no record is found
    }

    // Retrieves all patients from the database
    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Convert each result row into a Patient object
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    // Saves the patient: inserts if new, otherwise updates
    public Patient save(Patient patient) throws SQLException {
        if (patient.getPatientId() == null) {
            return insert(patient); // Insert new patient
        } else {
            return update(patient); // Update existing patient
        }
    }

    // Inserts a new patient into the database
    private Patient insert(Patient patient) throws SQLException {
        String sql = """
            INSERT INTO Patient (
              patientId, name, dateOfBirth, email, phone,
              emergencyContactName, emergencyContactPhone,
              createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Assign new UUID as the patient ID
            patient.setPatientId(UUID.randomUUID().toString());

            // Fill in the prepared statement parameters
            setStatementParameters(stmt, patient);

            stmt.executeUpdate(); // Execute INSERT
        }
        return patient;
    }

    // Updates an existing patient record
    private Patient update(Patient patient) throws SQLException {
        String sql = """
            UPDATE Patient
            SET
              name = ?, dateOfBirth = ?, email = ?, phone = ?,
              emergencyContactName = ?, emergencyContactPhone = ?,
              updatedAt = ?
            WHERE patientId = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Bind updated values
            stmt.setString(1, patient.getName());
            stmt.setDate(2,
                patient.getDateOfBirth() != null
                    ? Date.valueOf(patient.getDateOfBirth())
                    : null
            );
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getEmergencyContactName());
            stmt.setString(6, patient.getEmergencyContactPhone());
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
            stmt.setString(8, patient.getPatientId());

            stmt.executeUpdate(); // Execute UPDATE
        }
        return patient;
    }

    // Deletes a patient by ID
    public void delete(String patientId) throws SQLException {
        String sql = "DELETE FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId);
            stmt.executeUpdate();
        }
    }

    // Maps a ResultSet row to a Patient object
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();

        patient.setPatientId(rs.getString("patientId"));
        patient.setName(rs.getString("name"));

        // Handle nullable dateOfBirth
        Date dob = rs.getDate("dateOfBirth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }

        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setEmergencyContactName(rs.getString("emergencyContactName"));
        patient.setEmergencyContactPhone(rs.getString("emergencyContactPhone"));

        // Handle nullable createdAt and updatedAt
        Timestamp created = rs.getTimestamp("createdAt");
        if (created != null) {
            patient.setCreatedAt(created.toLocalDateTime());
        }
        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) {
            patient.setUpdatedAt(updated.toLocalDateTime());
        }

        return patient;
    }

    // Sets PreparedStatement parameters for inserting a patient
    private void setStatementParameters(PreparedStatement stmt, Patient patient)
            throws SQLException {
        stmt.setString(1, patient.getPatientId());
        stmt.setString(2, patient.getName());

        // Bind nullable dateOfBirth
        stmt.setDate(3,
            patient.getDateOfBirth() != null
                ? Date.valueOf(patient.getDateOfBirth())
                : null
        );

        stmt.setString(4, patient.getEmail());
        stmt.setString(5, patient.getPhone());
        stmt.setString(6, patient.getEmergencyContactName());
        stmt.setString(7, patient.getEmergencyContactPhone());

        // Set createdAt and updatedAt to now
        stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
    }
}
