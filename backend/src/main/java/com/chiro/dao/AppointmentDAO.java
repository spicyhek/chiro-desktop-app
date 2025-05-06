package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppointmentDAO {
    private final DatabaseConfig dbConfig;

    public AppointmentDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public AppointmentDAO(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Appointment findById(String appointmentId) throws SQLException {
        String sql = "SELECT * FROM Appointment WHERE appointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        }
        return null;
    }

    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointment";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public Appointment save(Appointment appointment) throws SQLException {
        if (appointment.getAppointmentId() == null) {
            return insert(appointment);
        } else {
            update(appointment);
            return appointment;
        }
    }

    private Appointment insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO Appointment (appointmentId, patientId, doctorId, scheduledDateTime, " +
                    "status, appointmentNotes, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            appointment.setAppointmentId(UUID.randomUUID().toString());
            setStatementParameters(stmt, appointment);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                appointment.setAppointmentId(String.valueOf(keys.getInt(1))); // store the generated ID
            }
        }
        return appointment;

    }

    private void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE Appointment SET patientId = ?, doctorId = ?, scheduledDateTime = ?, " +
                    "status = ?, appointmentNotes = ?, updatedAt = ? WHERE appointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointment.getPatientId());
            stmt.setString(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getAppointmentNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(7, appointment.getAppointmentId());
            stmt.executeUpdate();
        }
    }

    public void delete(String appointmentId) throws SQLException {
        String sql = "DELETE FROM Appointment WHERE appointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getString("appointmentId"));
        appointment.setPatientId(rs.getString("patientId"));
        appointment.setDoctorId(rs.getString("doctorId"));
        
        Timestamp scheduledDateTime = rs.getTimestamp("scheduledDateTime");
        appointment.setScheduledDateTime(scheduledDateTime != null ? scheduledDateTime.toLocalDateTime() : null);
        
        appointment.setStatus(rs.getString("status"));
        appointment.setAppointmentNotes(rs.getString("appointmentNotes"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        appointment.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updatedAt");
        appointment.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return appointment;
    }

    private void setStatementParameters(PreparedStatement stmt, Appointment appointment) throws SQLException {
        stmt.setString(1, appointment.getAppointmentId());
        stmt.setString(2, appointment.getPatientId());
        stmt.setString(3, appointment.getDoctorId());
        stmt.setTimestamp(4, Timestamp.valueOf(appointment.getScheduledDateTime()));
        stmt.setString(5, appointment.getStatus());
        stmt.setString(6, appointment.getAppointmentNotes());
        stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
    }
} 