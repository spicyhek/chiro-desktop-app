package com.chiro.controller;

import com.chiro.models.Doctor;
import com.chiro.models.MedicalRecord;
import com.chiro.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {
        try {
            doctorService.saveDoctor(doctor);
            return ResponseEntity.ok(doctor);
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
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error fetching doctors: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable String id) {
        try {
            Doctor doc = doctorService.getDoctorById(id);
            if (doc != null) {
                return ResponseEntity.ok(doc);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error fetching doctor: " + e.getMessage());
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDoctor(
//            @PathVariable String id,
//            @RequestBody Doctor updatedDoctor
//    ) {
//        try {
//            updatedDoctor.setDoctorId(id);
//            doctorService.saveDoctor(updatedDoctor);
//            return ResponseEntity.ok(updatedDoctor);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Validation error: " + e.getMessage());
//        } catch (SQLException e) {
//            return ResponseEntity
//                    .status(500)
//                    .body("Database error: " + e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable String id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Could not delete doctor: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            Doctor updated = doctorService.updateDoctorPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }
}
