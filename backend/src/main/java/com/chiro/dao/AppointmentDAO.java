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
        String sql = "SELECT * FROM appointments WHERE AppointmentId = ?";
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
        String sql = "SELECT * FROM appointments";
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public void save(Appointment appointment) throws SQLException {
        if (appointment.getAppointmentId() == null) {
            insert(appointment);
        } else {
            update(appointment);
        }
    }

    private void insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (AppointmentId, PatientId, DoctorId, ScheduledDateTime, Status, " +
                    "AppointmentNotes, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String appointmentId = UUID.randomUUID().toString();
            appointment.setAppointmentId(appointmentId);
            
            stmt.setString(1, appointmentId);
            stmt.setString(2, appointment.getPatientId());
            stmt.setString(3, appointment.getDoctorId());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.setString(6, appointment.getAppointmentNotes());
            stmt.setTimestamp(7, Timestamp.valueOf(now));
            stmt.setTimestamp(8, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
        }
    }

    private void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET PatientId = ?, DoctorId = ?, ScheduledDateTime = ?, " +
                    "Status = ?, AppointmentNotes = ?, UpdatedAt = ? WHERE AppointmentId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointment.getPatientId());
            stmt.setString(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getAppointmentNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(now));
            stmt.setString(7, appointment.getAppointmentId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(String appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE AppointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getString("AppointmentId"));
        appointment.setPatientId(rs.getString("PatientId"));
        appointment.setDoctorId(rs.getString("DoctorId"));
        
        Timestamp scheduledDateTime = rs.getTimestamp("ScheduledDateTime");
        appointment.setScheduledDateTime(scheduledDateTime != null ? scheduledDateTime.toLocalDateTime() : null);
        
        appointment.setStatus(rs.getString("Status"));
        appointment.setAppointmentNotes(rs.getString("AppointmentNotes"));
        
        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        appointment.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
        appointment.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
        
        return appointment;
    }
} 