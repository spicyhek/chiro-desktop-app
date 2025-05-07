package com.chiro.service;

import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Insurance;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceDAO insuranceDAO;

    // Constructor injection of the DAO bean
    public InsuranceService(InsuranceDAO insuranceDAO) {
        this.insuranceDAO = insuranceDAO;
    }

    public List<Insurance> getAllInsurances() throws SQLException {
        return insuranceDAO.findAll();
    }

    public Insurance getInsuranceById(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");
        Insurance insurance = insuranceDAO.findById(id);
        if (insurance == null) {
            throw new IllegalArgumentException("Insurance not found: " + id);
        }
        return insurance;
    }

    public Insurance saveInsurance(Insurance insurance) throws SQLException {
        ServiceValidationHelper.validateNotNull(insurance, "Insurance");

        String id = insurance.getInsuranceId();
        if (id != null && !id.isEmpty()) {
            ServiceValidationHelper.validateNotBlank(id, "Insurance ID");
        }
        ServiceValidationHelper.validateNotBlank(insurance.getInsuranceProvider(), "Insurance Provider");

        insuranceDAO.save(insurance);
        return insurance;
    }

    public void deleteInsurance(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");

        Insurance insurance = insuranceDAO.findById(id);
        if (insurance == null) {
            throw new IllegalArgumentException("Insurance not found: " + id);
        }
        insuranceDAO.delete(id);
    }
}
