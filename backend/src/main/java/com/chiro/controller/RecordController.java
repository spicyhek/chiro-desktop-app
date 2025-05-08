package com.chiro.controller;

import com.chiro.models.MedicalRecord;
import com.chiro.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody MedicalRecord record) {
        try {
            MedicalRecord saved = recordService.saveRecord(record);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable String id) {
        try {
            MedicalRecord rec = recordService.getRecordById(id);
            return ResponseEntity.ok(rec);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecords() {
        try {
            List<MedicalRecord> list = recordService.getAllRecords();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching records: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable String id) {
        try {
            recordService.deleteRecord(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        try {
            MedicalRecord updated = recordService.updateRecordPartial(id, updates);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Database error: " + e.getMessage());
        }
    }



}
