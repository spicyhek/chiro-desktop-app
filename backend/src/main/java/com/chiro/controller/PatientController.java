package com.chiro.controller;

import com.chiro.models.Patient;
import com.chiro.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient saved = patientService.savePatient(patient);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching patients: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable String id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updatePatient(@PathVariable String id, @RequestBody Patient updatedPatient) {
//        try {
//            updatedPatient.setPatientId(id);
//            Patient saved = patientService.savePatient(updatedPatient);
//            return ResponseEntity.ok(saved);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
//        } catch (SQLException e) {
//            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable String id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            Patient updated = patientService.updatePatientPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
}
