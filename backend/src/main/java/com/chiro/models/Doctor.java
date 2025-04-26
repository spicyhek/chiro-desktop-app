package com.chiro.models;

import java.time.LocalDateTime;

public class Doctor {
    private String doctorId;
    private String name;
    private String specialization;
    private String licenseNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Doctor() {}

    // Constructor with all fields
    public Doctor(String doctorId, String name, String specialization, String licenseNumber, 
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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