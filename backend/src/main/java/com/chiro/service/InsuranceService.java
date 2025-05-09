package com.chiro.service;

import com.chiro.dao.InsuranceDAO;
import com.chiro.models.Insurance;
import com.chiro.util.ServiceValidationHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class InsuranceService {

    private final InsuranceDAO insuranceDAO;

    public InsuranceService(InsuranceDAO insuranceDAO) {
        this.insuranceDAO = insuranceDAO;
    }

    @Transactional
    public Insurance saveInsurance(Insurance insurance) throws SQLException {
        ServiceValidationHelper.validateNotNull(insurance, "Insurance");
        ServiceValidationHelper.validateNotBlank(insurance.getInsuranceProvider(), "Insurance Provider");
        return insuranceDAO.save(insurance);
    }

    @Transactional(readOnly = true)
    public List<Insurance> getAllInsurances() throws SQLException {
        return insuranceDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Insurance getInsuranceById(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");
        return insuranceDAO.findById(id);
    }

    @Transactional
    public void deleteInsurance(String id) throws SQLException {
        ServiceValidationHelper.validateNotBlank(id, "Insurance ID");
        Insurance ins = insuranceDAO.findById(id);
        if (ins == null) {
            throw new IllegalArgumentException("Insurance not found: " + id);
        }
        insuranceDAO.delete(id);
    }
}