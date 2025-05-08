package com.chiro.service;

import com.chiro.dao.DoctorDAO;
import com.chiro.models.Doctor;
import com.chiro.models.MedicalRecord;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    private final DoctorDAO doctorDAO;

    public DoctorService(DoctorDAO doctorDAO) {
        this.doctorDAO = doctorDAO;
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        return doctorDAO.findAll();
    }

    public Doctor getDoctorById(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Doctor ID");
        Doctor doctor = doctorDAO.findById(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found: " + id);
        }
        return doctor;
    }

    public Doctor saveDoctor(Doctor doctor) throws SQLException {
        ServiceValidationHelper.validateNotNull(doctor, "Doctor");
        String id = doctor.getDoctorId();
        if (id != null && !id.isEmpty()) {
            ServiceValidationHelper.validateNotBlank(id, "Doctor ID");
        }
        ServiceValidationHelper.validateNotBlank(doctor.getName(), "Doctor Name");
        ServiceValidationHelper.validateNotBlank(doctor.getSpecialization(), "Doctor Specialization");
        ServiceValidationHelper.validateNotBlank(doctor.getLicenseNumber(), "Doctor License Number");
        return doctorDAO.save(doctor);
    }

    public void deleteDoctor(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Doctor ID");
        if (doctorDAO.findById(id) == null) {
            throw new IllegalArgumentException("Doctor not found: " + id);
        }
        doctorDAO.delete(id);
    }


    public Doctor updateDoctorPartial(String id, Map<String, Object> updates) throws SQLException {
        Doctor doctor = getDoctorById(id);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found for ID: " + id);
        }

        // Safely update name
        Object namesObj = updates.get("name");
        if (namesObj instanceof String name && !name.isBlank()) {
            doctor.setName(name);
        }

        // Safely update specialization
        Object specializationObj = updates.get("specialization");
        if (specializationObj instanceof String specialization && !specialization.isBlank()) {
            doctor.setSpecialization(specialization);
        }

        // Safely update license number
        Object licenseNumberObj = updates.get("licenseNumber");
        if (licenseNumberObj instanceof String licenseNumber && !licenseNumber.isBlank()) {
            doctor.setLicenseNumber(licenseNumber);
        }

        // Save and return the updated record
        doctorDAO.save(doctor);
        return doctor;
    }
}
