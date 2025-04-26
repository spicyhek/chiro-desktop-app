package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private final DatabaseConfig dbConfig;

    public AppointmentDAO() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    public Appointment findById(int appointmentId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE AppointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
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
        if (appointment.getAppointmentId() == 0) {
            insert(appointment);
        } else {
            update(appointment);
        }
    }

    private void insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (PatientId, DoctorId, ScheduledDateTime, Status, " +
                    "AppointmentNotes, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getAppointmentNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(now));
            stmt.setTimestamp(7, Timestamp.valueOf(now));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    appointment.setAppointmentId(rs.getInt(1));
                }
            }
        }
    }

    private void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointments SET PatientId = ?, DoctorId = ?, ScheduledDateTime = ?, " +
                    "Status = ?, AppointmentNotes = ?, UpdatedAt = ? WHERE AppointmentId = ?";
        LocalDateTime now = LocalDateTime.now();
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getPatientId());
            stmt.setInt(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getAppointmentNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(now));
            stmt.setInt(7, appointment.getAppointmentId());
            
            stmt.executeUpdate();
        }
    }

    public void delete(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE AppointmentId = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("AppointmentId"));
        appointment.setPatientId(rs.getInt("PatientId"));
        appointment.setDoctorId(rs.getInt("DoctorId"));
        
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