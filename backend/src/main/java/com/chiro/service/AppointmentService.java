package com.chiro.service;

import com.chiro.dao.AppointmentDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.Appointment;
import com.chiro.util.ServiceValidationHelper;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

// Service for Appointment DAOs. Checks if DAOs and their attributes are blank or null before querying. Call these methods in the controller
// Additional checks can be made if needed
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.patientDAO = new PatientDAO();
        this.doctorDAO = new DoctorDAO();
    }

    // Custom DAO constructor for testing purposes if needed
    public AppointmentService(AppointmentDAO appointmentDAO, PatientDAO patientDAO, DoctorDAO doctorDAO) {
        this.appointmentDAO = appointmentDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        return appointmentDAO.findAll();
    }

    public Appointment getAppointmentById(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Appointment ID");
        Appointment appointment = appointmentDAO.findById(id);
        if (appointment == null) {
            throw new SQLException("Appointment not found: " + id);
        }
        return appointment;
    }

    public void saveAppointment(Appointment appointment) throws SQLException {
        ServiceValidationHelper.validateNotNull(appointment, "Appointment");
        ServiceValidationHelper.validateNotBlank(appointment.getAppointmentId(), "Appointment ID");
        ServiceValidationHelper.validateNotBlank(appointment.getPatientId(), "Patient ID");
        ServiceValidationHelper.validateNotBlank(appointment.getDoctorId(), "Doctor ID");

        if (appointment.getScheduledDateTime() == null || appointment.getScheduledDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future.");
        }

        if (patientDAO.findById(appointment.getPatientId()) == null) {
            throw new IllegalArgumentException("Patient does not exist: " + appointment.getPatientId());
        }

        if (doctorDAO.findById(appointment.getDoctorId()) == null) {
            throw new IllegalArgumentException("Doctor does not exist: " + appointment.getDoctorId());
        }


        appointmentDAO.save(appointment);
    }

    public void deleteAppointment(String appointmentId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(appointmentId, "Appointment ID");

        if (appointmentDAO.findById(appointmentId) == null) {
            throw new IllegalArgumentException("Appointment does not exist: " + appointmentId);
            }

        appointmentDAO.delete(appointmentId);

    }
    // Once every check passes, the DAO is saved to the database.
}
