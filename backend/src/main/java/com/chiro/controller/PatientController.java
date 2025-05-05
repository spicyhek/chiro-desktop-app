package com.chiro.controller;

import com.chiro.models.Patient;
import com.chiro.service.PatientService;
import com.chiro.dao.PatientDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/*
Patient controller. Supports HTTP requests POST, GET (patients by ID and all patients), PUT (updates), and DELETE.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController() {
        this.patientService = new PatientService(new PatientDAO());
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            patientService.savePatient(patient);
            return ResponseEntity.ok("Patient saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error:" + e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning patients:" + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getPatientById(@PathVariable("id") String id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if(patient != null) {
                return ResponseEntity.ok(patient);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning patient:" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
        try {
            updatedPatient.setPatientId(id);
            patientService.savePatient(updatedPatient);
            return ResponseEntity.ok("Patient updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error updating patient: " + e.getMessage());
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePatient(@PathVariable("id") String id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok("Patient deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Could not delete patient: " + e.getMessage());
        }
    }
}
