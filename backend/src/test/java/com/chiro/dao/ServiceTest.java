package com.chiro.dao;

import com.chiro.models.*;
import com.chiro.service.*;
import com.chiro.util.TestDataSeeder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {

    @Autowired
    private DataSource dataSource;

    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private InsuranceService insuranceService;
    private RecordService recordService;
    private TestDataSeeder seeder;

    @BeforeAll
    public void initDatabase() throws Exception {
        // Read the entire schema.sql file and split on semicolons so we run each CREATE
        String ddl = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : ddl.split(";")) {
                sql = sql.trim();
                if (!sql.isEmpty()) {
                    stmt.execute(sql);
                }
            }
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        // Instantiate each DAO with the Spring-managed DataSource
        PatientDAO      patientDAO      = new PatientDAO(dataSource);
        DoctorDAO       doctorDAO       = new DoctorDAO(dataSource);
        AppointmentDAO  appointmentDAO  = new AppointmentDAO(dataSource);
        InsuranceDAO    insuranceDAO    = new InsuranceDAO(dataSource);
        RecordDAO       recordDAO       = new RecordDAO(dataSource);

        // Wire up your services
        this.patientService     = new PatientService(patientDAO);
        this.doctorService      = new DoctorService(doctorDAO);
        this.appointmentService = new AppointmentService(appointmentDAO, patientDAO, doctorDAO);
        this.insuranceService   = new InsuranceService(insuranceDAO);
        this.recordService      = new RecordService(recordDAO, patientDAO, doctorDAO);

        // Seed whatever baseline data you need
        this.seeder = new TestDataSeeder(patientDAO, doctorDAO);
        seeder.seedDefaultTestData();
    }

    // ────────── PATIENT TESTS ──────────

    @Test
    public void testSaveValidPatient_doesNotThrow() {
        Patient p = new Patient();
        p.setPatientId("test-patient-1");
        p.setName("Alice");
        p.setDateOfBirth(LocalDate.of(2000, 1, 1));
        p.setEmail("alice@example.com");

        assertDoesNotThrow(() -> patientService.savePatient(p));
    }

    @Test
    public void testSavePatient_missingName_throwsException() {
        Patient p = new Patient();
        p.setPatientId("test-patient-2");

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> patientService.savePatient(p)
        );
        assertTrue(ex.getMessage().contains("Patient name"));
    }

    @Test
    public void testSavePatient_invalidEmail_throwsException() {
        Patient p = new Patient();
        p.setPatientId("test-patient-3");
        p.setName("Bob");
        p.setEmail("invalid-email");

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> patientService.savePatient(p)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    public void testDeleteNonexistentPatient_throwsSQLException() {
        String id = "nonexistent-patient-id";
        Exception ex = assertThrows(SQLException.class,
                () -> patientService.deletePatient(id)
        );
        assertTrue(ex.getMessage().contains("Patient not found"));
    }

    // ────────── DOCTOR TESTS ──────────

    @Test
    public void testSaveValidDoctor_doesNotThrow() {
        Doctor d = new Doctor();
        d.setDoctorId("test-doctor-1");
        d.setName("Dr. Jane");
        d.setSpecialization("Orthopedics");
        d.setLicenseNumber("LIC-001");

        assertDoesNotThrow(() -> doctorService.saveDoctor(d));
    }

    // ────────── INSURANCE TESTS ──────────

    @Test
    public void testSaveValidInsurance_doesNotThrow() {
        Insurance i = new Insurance();
        i.setInsuranceId("INS-1");
        i.setInsuranceProvider("HealthNet");

        assertDoesNotThrow(() -> insuranceService.saveInsurance(i));
    }

    // ────────── RECORD TESTS ──────────

    @Test
    public void testSaveRecordWithMissingPatient_throwsException() {
        MedicalRecord r = new MedicalRecord();
        r.setRecordId("REC-1");
        r.setPatientId("nonexistent-patient");
        r.setDoctorId("test-doctor-1");
        r.setDiagnosis("Back pain");
        r.setVisitDate(java.time.LocalDate.now());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> recordService.saveRecord(r)
        );
        assertTrue(ex.getMessage().contains("Patient not found"));
    }
}
