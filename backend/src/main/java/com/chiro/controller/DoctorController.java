package com.chiro.controller;

import com.chiro.models.Doctor;
import com.chiro.service.DoctorService;
import com.chiro.dao.DoctorDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Doctor controller. Supports HTTP requests POST, GET (doctors by ID and all patients), PUT (updates), and DELETE.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController() {
        this.doctorService = new DoctorService();
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        try {
            Doctor saved = doctorService.saveDoctor(doctor);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Validation error: " + e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error returning doctors: " + e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable("id") String id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            if(doctor != null) {
                return ResponseEntity.ok(doctor);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error returning doctor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody Doctor updatedDoctor) {
        try {
            updatedDoctor.setDoctorId(id);
            doctorService.saveDoctor(updatedDoctor);
            return ResponseEntity.ok("Doctor updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error updating doctor: " + e.getMessage());
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("id") String id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Doctor deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not delete doctor: " + e.getMessage());
        }
    }
}
