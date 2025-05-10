package com.chiro.controller;

import com.chiro.models.Appointment;
import com.chiro.models.Doctor;
import com.chiro.models.Patient;
import com.chiro.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Spring will autowire the AppointmentService bean here
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByPatientAlias(@RequestParam("q") String q) {
        return searchByPatient(q);
    }

    @GetMapping("/searchByPatient")
    public ResponseEntity<?> searchByPatient(@RequestParam("q") String q) {
        try {
            List<Appointment> matches = appointmentService.searchAppointmentsByPatient(q);
            return ResponseEntity.ok(matches);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment saved = appointmentService.saveAppointment(appointment);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity
                    .status(500)
                    .body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        try {
            List<Appointment> all = appointmentService.getAllAppointments();
            return ResponseEntity.ok(all);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error fetching appointments: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        try {
            Appointment appt = appointmentService.getAppointmentById(id);
            if (appt != null) {
                return ResponseEntity.ok(appt);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error fetching appointment: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateAppointment(
//            @PathVariable String id,
//            @RequestBody Appointment updatedAppointment
//    ) {
//        try {
//            updatedAppointment.setAppointmentId(id);
//            Appointment saved = appointmentService.saveAppointment(updatedAppointment);
//            return ResponseEntity.ok(saved);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Validation error: " + e.getMessage());
//        } catch (SQLException e) {
//            return ResponseEntity
//                    .status(500)
//                    .body("Error updating appointment: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable String id) {
        try {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.ok().build();
        } catch (SQLException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Could not delete appointment: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            Appointment updated = appointmentService.updateAppointmentPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
}
