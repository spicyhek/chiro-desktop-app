package com.chiro.controller;

import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Insurance;
import com.chiro.service.InsuranceService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/*
Insurance controller. Supports HTTP requests POST, GET (insurance by ID and all insurances), PUT (updates), and DELETE.
 */
@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController() {
        this.insuranceService = new InsuranceService(new InsuranceDAO());
    }

    @PostMapping
    public ResponseEntity<?> createInsurance(@RequestBody Insurance insurance) {
        try {
            insuranceService.saveInsurance(insurance);
            return ResponseEntity.ok("Insurance saved successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInsuranceById(@PathVariable String id) {
        try {
            Insurance insurance = insuranceService.getInsuranceById(id);
            return ResponseEntity.ok(insurance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllInsurances() {
        try {
            List<Insurance> insurances = insuranceService.getAllInsurances();
            return ResponseEntity.ok(insurances);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning patient: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInsurance(@PathVariable String id, @RequestBody Insurance updatedInsurance) {
        try {
            updatedInsurance.setInsuranceId(id);
            insuranceService.saveInsurance(updatedInsurance);
            return ResponseEntity.ok("Patient updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error updating insurance: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInsurance(@PathVariable String id) {
        try {
            insuranceService.deleteInsurance(id);
            return ResponseEntity.ok("Insurance deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Could not delete patient: " + e.getMessage());
        }
    }
}
