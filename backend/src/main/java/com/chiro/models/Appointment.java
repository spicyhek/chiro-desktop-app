package com.chiro.models;

import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int patientId;
    private int doctorId;
    private LocalDateTime scheduledDateTime;
    private String status;
    private String appointmentNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Appointment() {}

    // Constructor with all fields
    public Appointment(int appointmentId, int patientId, int doctorId, LocalDateTime scheduledDateTime,
                      String status, String appointmentNotes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scheduledDateTime = scheduledDateTime;
        this.status = status;
        this.appointmentNotes = appointmentNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointmentNotes() {
        return appointmentNotes;
    }

    public void setAppointmentNotes(String appointmentNotes) {
        this.appointmentNotes = appointmentNotes;
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