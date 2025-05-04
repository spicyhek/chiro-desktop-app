package com.chiro.service;

import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Insurance;
import com.chiro.util.ServiceValidationHelper;
import java.sql.SQLException;
import java.util.List;

// Service for Insurance DAOs. Checks if DAOs and their attributes are blank or null before querying or deleting. Call these methods in the controller
// Additional checks can be made if needed
public class InsuranceService {

    private final InsuranceDAO insuranceDAO;

    public InsuranceService() {
        this.insuranceDAO = new InsuranceDAO();
    }

    public InsuranceService (InsuranceDAO insuranceDAO) {
        this.insuranceDAO = insuranceDAO;
    }

    public List<Insurance> getAllInsurances() throws SQLException {
        return insuranceDAO.findAll();
    }

    public Insurance getInsuranceById(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");
        Insurance insurance = insuranceDAO.findById(id);
        if (insurance == null) {
            throw new SQLException("Insurance not found");
        }
        return insurance;
    }

    public void saveInsurance(Insurance insurance) throws SQLException {
        ServiceValidationHelper.validateNotNull(insurance, "Insurance");

        if(insurance.getInsuranceId() != null) {
            ServiceValidationHelper.validateNotBlank(insurance.getInsuranceId(), "Insurance ID");
        }
        ServiceValidationHelper.validateNotBlank(insurance.getInsuranceProvider(), "Insurance Provider");
        insuranceDAO.save(insurance);
    }

    public void deleteInsurance(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");

        Insurance insurance = insuranceDAO.findById(id);
        if (insurance == null) {
            throw new SQLException("Insurance not found");
        }

        insuranceDAO.delete(id);
    }
}
