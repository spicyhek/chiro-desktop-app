package com.chiro.dao;

import com.chiro.models.Patient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository // Marks this class as a Spring-managed DAO component
public class PatientDAO {
    private final DataSource dataSource;

    // Constructor injection of Spring's configured DataSource
    public PatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper method to retrieve a JDBC Connection from the DataSource
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<Patient> searchByName(String nameFilter) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE name LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nameFilter + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                List<Patient> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapResultSetToPatient(rs));
                }
                return results;
            }
        }
    }


    // Retrieves a Patient by their unique ID
    public Patient findById(String patientId) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId); // Bind patient ID to query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs); // Map DB row to Patient object
                }
            }
        }
        return null; // Return null if not found
    }

    // Retrieves all Patients from the database
    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs)); // Map each row to Patient
            }
        }
        return patients;
    }

    // Saves the patient: inserts if new, otherwise updates
    public Patient save(Patient patient) throws SQLException {
        if (patient.getPatientId() == null) {
            return insert(patient); // No ID = new patient
        } else {
            return update(patient); // Existing patient
        }
    }

    // Inserts a new Patient record into the database
    private Patient insert(Patient patient) throws SQLException {
        String sql = """
            INSERT INTO Patient (
              patientId, name, dateOfBirth, email, phone,
              emergencyContactName, emergencyContactPhone,
              insuranceId,
              createdAt, updatedAt
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generate and assign UUID for new patient
            patient.setPatientId(UUID.randomUUID().toString());

            // Bind values to prepared statement
            setStatementParameters(stmt, patient);

            stmt.executeUpdate(); // Execute INSERT
        }
        return patient;
    }

    // Updates an existing Patient record in the database
    private Patient update(Patient patient) throws SQLException {
        String sql = """
            UPDATE Patient
            SET
              name = ?, dateOfBirth = ?, email = ?, phone = ?,
              emergencyContactName = ?, emergencyContactPhone = ?,
              insuranceId = ?, updatedAt = ?
            WHERE patientId = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Bind updated values in exact parameter order
            stmt.setString(1, patient.getName());
            stmt.setDate(2, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
            stmt.setString(3, patient.getEmail());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getEmergencyContactName());
            stmt.setString(6, patient.getEmergencyContactPhone());
            stmt.setString(7, patient.getInsuranceId());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // updatedAt = now
            stmt.setString(9, patient.getPatientId()); // WHERE condition

            stmt.executeUpdate(); // Execute UPDATE
        }
        return patient;
    }

    // Deletes a patient by their ID
    public void delete(String patientId) throws SQLException {
        String sql = "DELETE FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patientId); // Bind patient ID
            stmt.executeUpdate(); // Execute DELETE
        }
    }

    // Converts a ResultSet row into a Patient object
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();

        patient.setPatientId(rs.getString("patientId"));
        patient.setName(rs.getString("name"));

        // Handle nullable date of birth
        Date dob = rs.getDate("dateOfBirth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }

        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setEmergencyContactName(rs.getString("emergencyContactName"));
        patient.setEmergencyContactPhone(rs.getString("emergencyContactPhone"));

        // Map optional insuranceId
        patient.setInsuranceId(rs.getString("insuranceId"));

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

    // Binds parameters for a new patient INSERT operation
    private void setStatementParameters(PreparedStatement stmt, Patient patient)
            throws SQLException {
        stmt.setString(1, patient.getPatientId());
        stmt.setString(2, patient.getName());
        stmt.setDate(3, patient.getDateOfBirth() != null ? Date.valueOf(patient.getDateOfBirth()) : null);
        stmt.setString(4, patient.getEmail());
        stmt.setString(5, patient.getPhone());
        stmt.setString(6, patient.getEmergencyContactName());
        stmt.setString(7, patient.getEmergencyContactPhone());
        stmt.setString(8, patient.getInsuranceId());
        stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now())); // createdAt = now
        stmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now())); // updatedAt = now
    }
}