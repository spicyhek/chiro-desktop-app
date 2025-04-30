package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.*;
import com.chiro.models.Record;
import com.chiro.service.*;

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
        this.patientService = new PatientService(new PatientDAO());
        this.doctorService = new DoctorService(new DoctorDAO());
        this.appointmentService = new AppointmentService(new AppointmentDAO(), new PatientDAO(), new DoctorDAO());
        this.insuranceService = new InsuranceService(new InsuranceDAO());
        this.recordService = new RecordService(new RecordDAO(), new PatientDAO(), new DoctorDAO());

        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();

        Patient patient = new Patient();
        patient.setPatientId("test-patient-1");
        patient.setName("Test Patient");
        patientDAO.save(patient);

        Doctor doctor = new Doctor();
        doctor.setDoctorId("test-doctor-1");
        doctor.setName("Test Doctor");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("D123");
        doctorDAO.save(doctor);
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

    // ──────────────── APPOINTMENT SERVICE TESTS ────────────────

    @Test
    public void testSaveValidAppointment_doesNotThrow() throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId("APT-1");
        appointment.setPatientId("test-patient-1");
        appointment.setDoctorId("test-doctor-1");
        appointment.setScheduledDateTime(LocalDateTime.now().plusDays(1));

        assertDoesNotThrow(() -> appointmentService.saveAppointment(appointment));
    }

    // ──────────────── RECORD SERVICE TESTS ────────────────

    @Test
    public void testSaveRecordWithMissingPatient_throwsException() {
        com.chiro.models.Record record = new com.chiro.models.Record();
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
