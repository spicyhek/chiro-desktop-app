package com.chiro.controller;

import com.chiro.models.Insurance;
import com.chiro.models.Patient;
import com.chiro.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    // Constructor-injection of the Spring-managed service
    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Insurance>> searchInsurance(@RequestParam("q") String query) {
        List<Insurance> results = insuranceService.searchInsurance(query);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<?> createInsurance(@RequestBody Insurance insurance) {
        try {
            Insurance saved = insuranceService.saveInsurance(insurance);
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getInsuranceById(@PathVariable String id) {
        try {
            Insurance ins = insuranceService.getInsuranceById(id);
            if (ins != null) {
                return ResponseEntity.ok(ins);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity
                    .status(500)
                    .body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllInsurances() {
        try {
            List<Insurance> list = insuranceService.getAllInsurances();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("Error fetching insurances: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInsurance(
            @PathVariable String id,
            @RequestBody Insurance updatedInsurance
    ) {
        try {
            updatedInsurance.setInsuranceId(id);
            Insurance saved = insuranceService.updateInsurance(id, updatedInsurance);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInsurance(@PathVariable String id) {
        try {
            insuranceService.deleteInsurance(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity
                    .status(500)
                    .body("Database error: " + e.getMessage());
        }
    }
}
