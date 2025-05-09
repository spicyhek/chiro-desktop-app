package com.chiro.dao;

import com.chiro.models.Appointment;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository // Marks this class as a Spring-managed bean, specifically for data access
public class AppointmentDAO {
    private final DataSource dataSource;

    // Constructor injection of the DataSource (typically configured by Spring Boot)
    public AppointmentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Helper method to get a database connection from the DataSource
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Finds a single appointment by its ID
    public Appointment findById(String appointmentId) throws SQLException {
        String sql = "SELECT * FROM Appointment WHERE appointmentId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentId); // Bind the appointment ID
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // If a row is found, convert it to an Appointment object
                    return mapResultSetToAppointment(rs);
                }
            }
        }
        return null; // Return null if not found
    }

    // Retrieves all appointments from the database
    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointment";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Convert each row into an Appointment object
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    // Saves an appointment: inserts a new one or updates an existing one
    public Appointment save(Appointment appointment) throws SQLException {
        if (appointment.getAppointmentId() == null) {
            return insert(appointment); // Insert if it's new
        } else {
            update(appointment);       // Otherwise, update existing
            return appointment;
        }
    }

    // Inserts a new appointment into the database
    private Appointment insert(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO Appointment " +
                "(appointmentId, patientId, doctorId, scheduledDateTime, status, appointmentNotes, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Generate a new UUID as the appointment ID
            appointment.setAppointmentId(UUID.randomUUID().toString());

            // Set the statement parameters from the appointment object
            setStatementParameters(stmt, appointment);
            stmt.executeUpdate();
        }
        return appointment;
    }

    // Updates an existing appointment
    private void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE Appointment SET " +
                "patientId = ?, doctorId = ?, scheduledDateTime = ?, " +
                "status = ?, appointmentNotes = ?, updatedAt = ? " +
                "WHERE appointmentId = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set each parameter explicitly
            stmt.setString(1, appointment.getPatientId());
            stmt.setString(2, appointment.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getScheduledDateTime()));
            stmt.setString(4, appointment.getStatus());
            stmt.setString(5, appointment.getAppointmentNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
            stmt.setString(7, appointment.getAppointmentId());

            stmt.executeUpdate();
        }
    }

    // Deletes an appointment by its ID
    public void delete(String appointmentId) throws SQLException {
        String sql = "DELETE FROM Appointment WHERE appointmentId = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    // Converts a row from the ResultSet into an Appointment object
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getString("appointmentId"));
        appointment.setPatientId(rs.getString("patientId"));
        appointment.setDoctorId(rs.getString("doctorId"));

        // Safely handle null timestamps
        Timestamp dt = rs.getTimestamp("scheduledDateTime");
        appointment.setScheduledDateTime(dt != null ? dt.toLocalDateTime() : null);

        appointment.setStatus(rs.getString("status"));
        appointment.setAppointmentNotes(rs.getString("appointmentNotes"));

        Timestamp created = rs.getTimestamp("createdAt");
        if (created != null) {
            appointment.setCreatedAt(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updatedAt");
        if (updated != null) {
            appointment.setUpdatedAt(updated.toLocalDateTime());
        }

        return appointment;
    }

    // Sets the parameters on a PreparedStatement based on an Appointment object
    private void setStatementParameters(PreparedStatement stmt, Appointment appointment)
            throws SQLException {
        stmt.setString(1, appointment.getAppointmentId());
        stmt.setString(2, appointment.getPatientId());
        stmt.setString(3, appointment.getDoctorId());
        stmt.setTimestamp(4, Timestamp.valueOf(appointment.getScheduledDateTime()));
        stmt.setString(5, appointment.getStatus());
        stmt.setString(6, appointment.getAppointmentNotes());
        stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now())); // createdAt
        stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // updatedAt
    }
}
