package com.chiro.dao;

import com.chiro.models.Patient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class PatientDAO {
    private final DataSource dataSource;

    public PatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Patient findById(String patientId) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
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
        String sql = "SELECT * FROM Patient";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }
        }
        return patients;
    }

    public Patient save(Patient patient) throws SQLException {
        if (patient.getPatientId() == null) {
            return insert(patient);
        } else {
            return update(patient);
        }
    }

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

            patient.setPatientId(UUID.randomUUID().toString());
            setStatementParameters(stmt, patient);
            stmt.executeUpdate();
        }
        return patient;
    }

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
            stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(8, patient.getPatientId());
            stmt.executeUpdate();
        }
        return patient;
    }

    public void delete(String patientId) throws SQLException {
        String sql = "DELETE FROM Patient WHERE patientId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            stmt.executeUpdate();
        }
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientId(rs.getString("patientId"));
        patient.setName(rs.getString("name"));

        Date dob = rs.getDate("dateOfBirth");
        if (dob != null) {
            patient.setDateOfBirth(dob.toLocalDate());
        }

        patient.setEmail(rs.getString("email"));
        patient.setPhone(rs.getString("phone"));
        patient.setEmergencyContactName(rs.getString("emergencyContactName"));
        patient.setEmergencyContactPhone(rs.getString("emergencyContactPhone"));

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

    private void setStatementParameters(PreparedStatement stmt, Patient patient)
            throws SQLException {
        stmt.setString(1, patient.getPatientId());
        stmt.setString(2, patient.getName());
        stmt.setDate(3,
                patient.getDateOfBirth() != null
                        ? Date.valueOf(patient.getDateOfBirth())
                        : null
        );
        stmt.setString(4, patient.getEmail());
        stmt.setString(5, patient.getPhone());
        stmt.setString(6, patient.getEmergencyContactName());
        stmt.setString(7, patient.getEmergencyContactPhone());
        stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
    }
}
