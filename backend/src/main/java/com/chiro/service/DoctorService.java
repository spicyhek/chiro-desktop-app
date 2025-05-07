package com.chiro.service;

import com.chiro.dao.DoctorDAO;
import com.chiro.models.Doctor;
import com.chiro.util.ServiceValidationHelper;
import java.sql.SQLException;
import java.util.List;

// Service for Doctor DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting. Call these methods in the controller
// Additional checks can be made if needed
public class DoctorService {

    private final DoctorDAO doctorDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
    }

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
            throw new SQLException("Doctor not found: " + id);
        }
        return doctor;
    }

    public Doctor saveDoctor(Doctor doctor) throws SQLException {
        ServiceValidationHelper.validateNotNull(doctor, "Doctor");

        if (doctor.getDoctorId() != null) {
            ServiceValidationHelper.validateNotBlank(doctor.getDoctorId(), "Doctor ID");
        }

        ServiceValidationHelper.validateNotBlank(doctor.getName(), "Doctor Name");
        ServiceValidationHelper.validateNotBlank(doctor.getSpecialization(), "Doctor Specialization");
        ServiceValidationHelper.validateNotBlank(doctor.getLicenseNumber(), "Doctor License Number");

        return doctorDAO.save(doctor);
    }

    public void deleteDoctor(String doctorId) throws SQLException {
        ServiceValidationHelper.validateNotBlank(doctorId, "Doctor ID");

        Doctor doctor = doctorDAO.findById(doctorId);
        if (doctor == null) {
            throw new SQLException("Doctor not found: " + doctorId);
        }

        doctorDAO.delete(doctorId);
    }
}
