package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Record;
import com.chiro.models.*;
import com.chiro.service.*;
import com.chiro.util.TestDataSeeder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private InsuranceService insuranceService;
    private RecordService recordService;

    private TestDataSeeder seeder;

    @BeforeAll
    public static void initDatabase() throws Exception {
        Connection conn = DatabaseConfig.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        String schemaSql = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
        stmt.executeUpdate(schemaSql);
        stmt.close();
    }

    @BeforeEach
    public void setup() throws SQLException {
        DatabaseConfig config = DatabaseConfig.getInstance();

        PatientDAO patientDAO = new PatientDAO(config);
        DoctorDAO doctorDAO = new DoctorDAO(config);
        AppointmentDAO appointmentDAO = new AppointmentDAO(config);
        InsuranceDAO insuranceDAO = new InsuranceDAO(config);
        RecordDAO recordDAO = new RecordDAO(config);

        this.patientService = new PatientService(patientDAO);
        this.doctorService = new DoctorService(doctorDAO);
        this.appointmentService = new AppointmentService(appointmentDAO, patientDAO, doctorDAO);
        this.insuranceService = new InsuranceService(insuranceDAO);
        this.recordService = new RecordService(recordDAO, patientDAO, doctorDAO);

        this.seeder = new TestDataSeeder(patientDAO, doctorDAO);
        seeder.seedDefaultTestData();
    }

    // ──────────────── PATIENT SERVICE TESTS ────────────────

    @Test
    public void testSaveValidPatient_doesNotThrow() {
        Patient patient = new Patient();
        patient.setPatientId("test-patient-1");
        patient.setName("Alice");
        patient.setDateOfBirth(LocalDate.of(2000, 1, 1));
        patient.setEmail("alice@example.com");

        assertDoesNotThrow(() -> patientService.savePatient(patient));
    }

    @Test
    public void testSavePatient_missingName_throwsException() {
        Patient patient = new Patient();
        patient.setPatientId("test-patient-2");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.savePatient(patient);
        });

        assertTrue(exception.getMessage().contains("Patient name"));
    }

    @Test
    public void testSavePatient_invalidEmail_throwsException() {
        Patient patient = new Patient();
        patient.setPatientId("test-patient-3");
        patient.setName("Bob");
        patient.setEmail("invalid-email");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            patientService.savePatient(patient);
        });

        assertTrue(exception.getMessage().toLowerCase().contains("email"));
    }

    @Test
    public void testDeleteNonexistentPatient_throwsSQLException() {
        String nonExistentId = "nonexistent-patient-id";

        Exception exception = assertThrows(SQLException.class, () -> {
            patientService.deletePatient(nonExistentId);
        });

        assertTrue(exception.getMessage().contains("Patient not found"));
    }

    // ──────────────── DOCTOR SERVICE TESTS ────────────────

    @Test
    public void testSaveValidDoctor_doesNotThrow() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId("test-doctor-1");
        doctor.setName("Dr. Jane");
        doctor.setSpecialization("Orthopedics");
        doctor.setLicenseNumber("LIC-001");

        assertDoesNotThrow(() -> doctorService.saveDoctor(doctor));
    }

    // ──────────────── INSURANCE SERVICE TESTS ────────────────

    @Test
    public void testSaveValidInsurance_doesNotThrow() {
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-1");
        insurance.setInsuranceProvider("HealthNet");

        assertDoesNotThrow(() -> insuranceService.saveInsurance(insurance));
    }

    // ──────────────── RECORD SERVICE TESTS ────────────────

    @Test
    public void testSaveRecordWithMissingPatient_throwsException() {
        Record record = new Record();
        record.setRecordId("REC-1");
        record.setPatientId("nonexistent-patient");
        record.setDoctorId("test-doctor-1");
        record.setDiagnosis("Back pain");
        record.setVisitDate(LocalDate.now());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            recordService.saveRecord(record);
        });

        assertTrue(exception.getMessage().contains("Patient not found"));
    }
}