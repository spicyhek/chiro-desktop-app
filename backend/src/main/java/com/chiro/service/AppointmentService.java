package com.chiro.service;

import com.chiro.dao.AppointmentDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.Appointment;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentDAO appointmentDAO;
    private final PatientDAO patientDAO;
    private final DoctorDAO doctorDAO;

    public AppointmentService(
            AppointmentDAO appointmentDAO,
            PatientDAO patientDAO,
            DoctorDAO doctorDAO
    ) {
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
            throw new IllegalArgumentException("Appointment not found: " + id);
        }
        return appointment;
    }

    public Appointment saveAppointment(Appointment appointment) throws SQLException {
        ServiceValidationHelper.validateNotNull(appointment, "Appointment");
        ServiceValidationHelper.validateNotBlank(appointment.getPatientId(), "Patient ID");
        ServiceValidationHelper.validateNotBlank(appointment.getDoctorId(), "Doctor ID");

        LocalDateTime scheduled = appointment.getScheduledDateTime();
        if (scheduled == null || scheduled.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment date must be in the future.");
        }

        if (patientDAO.findById(appointment.getPatientId()) == null) {
            throw new IllegalArgumentException("Patient does not exist: " + appointment.getPatientId());
        }
        if (doctorDAO.findById(appointment.getDoctorId()) == null) {
            throw new IllegalArgumentException("Doctor does not exist: " + appointment.getDoctorId());
        }

        appointmentDAO.save(appointment);
        return appointment;
    }


    public void deleteAppointment(String appointmentId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(appointmentId, "Appointment ID");
        if (appointmentDAO.findById(appointmentId) == null) {
            throw new IllegalArgumentException("Appointment does not exist: " + appointmentId);
        }
        appointmentDAO.delete(appointmentId);
    }
}
