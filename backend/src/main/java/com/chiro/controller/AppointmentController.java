package com.chiro.controller;

import com.chiro.models.Appointment;
import com.chiro.service.AppointmentService;
import com.chiro.dao.AppointmentDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.DoctorDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/*
Appointment controller. Supports HTTP requests POST, GET (appointments by ID and all appointments), PUT (updates), and DELETE.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService(new AppointmentDAO(), new PatientDAO(), new DoctorDAO());
    }

//    @PostMapping
//    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
//        try {
//            appointmentService.saveAppointment(appointment);
//            return ResponseEntity.ok("Appointment saved successfully");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
//        } catch (SQLException e) {
//            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
//        }
//
//    }
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment saved = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(saved); // Return JSON object
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning patients: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("id") String id) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            if(appointment != null) {
                return ResponseEntity.ok(appointment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning appointment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable String id, @RequestBody Appointment updatedAppointment) {
        try {
            updatedAppointment.setAppointmentId(id);
            appointmentService.saveAppointment(updatedAppointment);
            return ResponseEntity.ok("Appointment updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error updating appointment: " + e.getMessage());
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable String id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok("Appointment deleted successfully");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Could not delete appointment: " + id);
        }
    }



}
