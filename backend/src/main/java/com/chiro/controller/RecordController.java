package com.chiro.controller;

import com.chiro.dao.DoctorDAO;
import com.chiro.dao.PatientDAO;
import com.chiro.dao.RecordDAO;
import com.chiro.models.Record;
import com.chiro.service.RecordService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/*
Record controller. Supports HTTP requests POST, GET (records by ID and all records), and DELETE.
Does not allow PUT; records should be immutable once created.
 */
@RestController
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController() {
        this.recordService = new RecordService(new RecordDAO(), new PatientDAO(), new DoctorDAO());
    }

    @PostMapping
    public ResponseEntity<?> createRecord(@RequestBody Record record) {
        try {
            recordService.saveRecord(record);
            return ResponseEntity.ok("Record saved successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable String id) {
        try {
            Record record = recordService.getRecordById(id);
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Database error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecords() {
        try {
            List<Record> records = recordService.getAllRecords();
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error returning records: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable String id) {
        try {
            recordService.deleteRecord(id);
            return ResponseEntity.ok("Record deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Could not delete record: " + e.getMessage());
        }
    }
}
