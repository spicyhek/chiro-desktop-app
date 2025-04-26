package com.chiro.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Record {
    private int recordId;
    private int appointmentId;
    private LocalDate visitDate;
    private String symptoms;
    private String diagnosis;
    private LocalDate nextVisitRecommendedDate;
    private String recordNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Record() {}

    // Constructor with all fields
    public Record(int recordId, int appointmentId, LocalDate visitDate, String symptoms,
                 String diagnosis, LocalDate nextVisitRecommendedDate, String recordNotes,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.visitDate = visitDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.nextVisitRecommendedDate = nextVisitRecommendedDate;
        this.recordNotes = recordNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public LocalDate getNextVisitRecommendedDate() {
        return nextVisitRecommendedDate;
    }

    public void setNextVisitRecommendedDate(LocalDate nextVisitRecommendedDate) {
        this.nextVisitRecommendedDate = nextVisitRecommendedDate;
    }

    public String getRecordNotes() {
        return recordNotes;
    }

    public void setRecordNotes(String recordNotes) {
        this.recordNotes = recordNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 