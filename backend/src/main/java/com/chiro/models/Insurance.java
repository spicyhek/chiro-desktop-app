package com.chiro.models;

import java.time.LocalDateTime;

public class Insurance {
    private String insuranceId;
    private String insuranceProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Insurance() {}

    // Constructor with all fields
    public Insurance(String insuranceId, String insuranceProvider, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.insuranceId = insuranceId;
        this.insuranceProvider = insuranceProvider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
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