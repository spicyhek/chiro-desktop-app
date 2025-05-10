package com.chiro.service;

import com.chiro.dao.AppointmentDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;
import com.chiro.models.Appointment;
import com.chiro.models.Doctor;
import com.chiro.models.Patient;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

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



    public List<Appointment> searchAppointmentsByPatient(String nameFilter) throws SQLException {
        ServiceValidationHelper.validateNotBlank(nameFilter, "Patient name filter");
        return appointmentDAO.searchByPatientName(nameFilter);
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

    public Appointment updateAppointmentPartial(String id, Map<String, Object> updates) throws SQLException {
        Appointment appt = getAppointmentById(id);
        if (appt == null) {
            throw new IllegalArgumentException("Appointment not found for ID: " + id);
        }

        // update patient id
        Object patientIdObj = updates.get("patientId");
        if (patientIdObj instanceof String patientId && !patientId.isBlank()) {
            appt.setPatientId(patientId);
        }

        // update doctor id
        Object doctorIdObj = updates.get("doctorId");
        if (doctorIdObj instanceof String doctorId && !doctorId.isBlank()) {
            appt.setDoctorId(doctorId);
        }

        // update scheduled date time
        Object nextScheduleObj = updates.get("scheduledDateTime");
        if (nextScheduleObj instanceof String scheduledDateTime && !scheduledDateTime.isBlank()) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(scheduledDateTime);
                appt.setScheduledDateTime(localDateTime);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date-time format for scheduledDateTime");
            }
        }

        // update status
        Object statusObj = updates.get("status");
        if (statusObj instanceof String status && !status.isBlank()) {
            appt.setStatus(status);
        }

        // update appointment notes
        Object appointmentNotesObj = updates.get("appointmentNotes");
        if (appointmentNotesObj instanceof String appointmentNotes && !appointmentNotes.isBlank()) {
            appt.setAppointmentNotes(appointmentNotes);
        }


        // Save and return the updated appointment
        appointmentDAO.save(appt);
        return appt;
    }

}
