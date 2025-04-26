package com.chiro.dao;

import com.chiro.config.DatabaseConfig;
import com.chiro.models.Appointment;
import com.chiro.models.Doctor;
import com.chiro.models.Insurance;
import com.chiro.models.Patient;
import com.chiro.util.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static PatientDAO patientDAO;
    private static DoctorDAO doctorDAO;
    private static InsuranceDAO insuranceDAO;
    private static AppointmentDAO appointmentDAO;
    private static RecordDAO recordDAO;
    private static DatabaseConfig dbConfig;

    @BeforeAll
    static void setup() throws SQLException {
        // Initialize database with test-specific database file
        dbConfig = DatabaseConfig.getInstance("jdbc:sqlite:test.db");
        DatabaseInitializer initializer = new DatabaseInitializer(dbConfig);
        initializer.initializeDatabase();

        // Initialize DAOs with test database configuration
        patientDAO = new PatientDAO(dbConfig);
        doctorDAO = new DoctorDAO(dbConfig);
        insuranceDAO = new InsuranceDAO(dbConfig);
        appointmentDAO = new AppointmentDAO(dbConfig);
        recordDAO = new RecordDAO(dbConfig);
    }

    @AfterAll
    static void cleanup() {
        // Delete the test database file
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Path.of("test.db"));
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }

    @BeforeEach
    void clearDatabase() throws SQLException {
        // Initialize database first to ensure tables exist
        DatabaseInitializer initializer = new DatabaseInitializer(dbConfig);
        initializer.initializeDatabase();
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            // Disable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = OFF");
            
            // Clear tables in reverse order of dependencies
            String[] tables = {"Record", "Appointment", "Patient", "Doctor", "Insurance"};
            for (String table : tables) {
                try {
                    stmt.execute("DELETE FROM " + table);
                } catch (SQLException e) {
                    // If table doesn't exist, initialize database and try again
                    if (e.getMessage().contains("no such table")) {
                        initializer.initializeDatabase();
                        stmt.execute("DELETE FROM " + table);
                    } else {
                        throw e;
                    }
                }
            }
            
            // Re-enable foreign key constraints
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    @Test
    void testInsuranceOperations() throws SQLException {
        // Create insurance
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS123");
        insurance.setInsuranceProvider("Test Insurance Co");
        insuranceDAO.save(insurance);
        assertNotNull(insurance.getInsuranceId());
        assertEquals("INS123", insurance.getInsuranceId());

        // Read insurance
        Insurance retrievedInsurance = insuranceDAO.findById(insurance.getInsuranceId());
        assertNotNull(retrievedInsurance);
        assertEquals("Test Insurance Co", retrievedInsurance.getInsuranceProvider());

        // Update insurance
        retrievedInsurance.setInsuranceProvider("Updated Insurance Co");
        insuranceDAO.save(retrievedInsurance);
        Insurance updatedInsurance = insuranceDAO.findById(retrievedInsurance.getInsuranceId());
        assertEquals("Updated Insurance Co", updatedInsurance.getInsuranceProvider());

        // Delete insurance
        insuranceDAO.delete(retrievedInsurance.getInsuranceId());
        assertNull(insuranceDAO.findById(retrievedInsurance.getInsuranceId()));
    }

    @Test
    void testInsuranceIdRequired() {
        Insurance insurance = new Insurance();
        insurance.setInsuranceProvider("Test Insurance Co");
        
        assertThrows(IllegalArgumentException.class, () -> {
            insuranceDAO.save(insurance);
        });
    }

    @Test
    void testPatientOperations() throws SQLException {
        // Create insurance first (required for patient)
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-PAT-001");
        insurance.setInsuranceProvider("Patient Test Insurance");
        insuranceDAO.save(insurance);

        // Create patient
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setEmail("john@example.com");
        patient.setPhone("123-456-7890");
        patient.setEmergencyContactName("Jane Doe");
        patient.setEmergencyContactPhone("987-654-3210");
        patientDAO.save(patient);
        assertNotNull(patient.getPatientId());
        assertTrue(patient.getPatientId().length() > 0);

        // Read patient
        Patient retrievedPatient = patientDAO.findById(patient.getPatientId());
        assertNotNull(retrievedPatient);
        assertEquals("John Doe", retrievedPatient.getName());

        // Update patient
        retrievedPatient.setName("John Smith");
        patientDAO.save(retrievedPatient);
        Patient updatedPatient = patientDAO.findById(retrievedPatient.getPatientId());
        assertEquals("John Smith", updatedPatient.getName());

        // Delete patient
        patientDAO.delete(retrievedPatient.getPatientId());
        assertNull(patientDAO.findById(retrievedPatient.getPatientId()));
    }

    @Test
    void testDoctorOperations() throws SQLException {
        // Create doctor
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setSpecialization("Chiropractic");
        doctor.setLicenseNumber("CHIRO123");
        doctorDAO.save(doctor);
        assertNotNull(doctor.getDoctorId());
        assertTrue(doctor.getDoctorId().length() > 0);

        // Read doctor
        Doctor retrievedDoctor = doctorDAO.findById(doctor.getDoctorId());
        assertNotNull(retrievedDoctor);
        assertEquals("Dr. Smith", retrievedDoctor.getName());
        assertEquals("Chiropractic", retrievedDoctor.getSpecialization());

        // Update doctor
        retrievedDoctor.setSpecialization("Sports Medicine");
        doctorDAO.save(retrievedDoctor);
        Doctor updatedDoctor = doctorDAO.findById(retrievedDoctor.getDoctorId());
        assertEquals("Sports Medicine", updatedDoctor.getSpecialization());

        // Delete doctor
        doctorDAO.delete(retrievedDoctor.getDoctorId());
        assertNull(doctorDAO.findById(retrievedDoctor.getDoctorId()));
    }

    @Test
    void testAppointmentOperations() throws SQLException {
        // Create required entities first
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-APT-001");
        insurance.setInsuranceProvider("Appointment Test Insurance");
        insuranceDAO.save(insurance);

        Patient patient = new Patient();
        patient.setName("Appointment Patient");
        patient.setEmail("appointment@example.com");
        patient.setPhone("123-456-7890");
        patientDAO.save(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Appointment Doctor");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("DOC123");
        doctorDAO.save(doctor);

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setDoctorId(doctor.getDoctorId());
        appointment.setScheduledDateTime(LocalDateTime.now().plusDays(1));
        appointment.setStatus("Scheduled");
        appointment.setAppointmentNotes("Initial consultation");
        appointmentDAO.save(appointment);
        assertNotNull(appointment.getAppointmentId());
        assertTrue(appointment.getAppointmentId().length() > 0);

        // Read appointment
        Appointment retrievedAppointment = appointmentDAO.findById(appointment.getAppointmentId());
        assertNotNull(retrievedAppointment);
        assertEquals(patient.getPatientId(), retrievedAppointment.getPatientId());
        assertEquals(doctor.getDoctorId(), retrievedAppointment.getDoctorId());
        assertEquals("Scheduled", retrievedAppointment.getStatus());

        // Update appointment
        retrievedAppointment.setStatus("Completed");
        appointmentDAO.save(retrievedAppointment);
        Appointment updatedAppointment = appointmentDAO.findById(retrievedAppointment.getAppointmentId());
        assertEquals("Completed", updatedAppointment.getStatus());

        // Delete appointment
        appointmentDAO.delete(retrievedAppointment.getAppointmentId());
        assertNull(appointmentDAO.findById(retrievedAppointment.getAppointmentId()));
    }

    @Test
    void testRecordOperations() throws SQLException {
        // Create required entities first
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-REC-001");
        insurance.setInsuranceProvider("Record Test Insurance");
        insuranceDAO.save(insurance);

        Patient patient = new Patient();
        patient.setName("Record Patient");
        patient.setEmail("record@example.com");
        patient.setPhone("123-456-7890");
        patientDAO.save(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Record Doctor");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("DOC456");
        doctorDAO.save(doctor);

        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setDoctorId(doctor.getDoctorId());
        appointment.setScheduledDateTime(LocalDateTime.now());
        appointment.setStatus("Completed");
        appointmentDAO.save(appointment);

        // Create record
        com.chiro.models.Record record = new com.chiro.models.Record();
        record.setAppointmentId(appointment.getAppointmentId());
        record.setVisitDate(LocalDate.now());
        record.setSymptoms("Back pain");
        record.setDiagnosis("Muscle strain");
        record.setNextVisitRecommendedDate(LocalDate.now().plusWeeks(2));
        record.setNotes("Patient responded well to treatment");
        recordDAO.save(record);
        assertNotNull(record.getRecordId());
        assertTrue(record.getRecordId().length() > 0);

        // Read record
        com.chiro.models.Record retrievedRecord = recordDAO.findById(record.getRecordId());
        assertNotNull(retrievedRecord);
        assertEquals(appointment.getAppointmentId(), retrievedRecord.getAppointmentId());
        assertEquals("Back pain", retrievedRecord.getSymptoms());
        assertEquals("Muscle strain", retrievedRecord.getDiagnosis());

        // Update record
        retrievedRecord.setDiagnosis("Improved muscle strain");
        recordDAO.save(retrievedRecord);
        com.chiro.models.Record updatedRecord = recordDAO.findById(retrievedRecord.getRecordId());
        assertEquals("Improved muscle strain", updatedRecord.getDiagnosis());

        // Delete record
        recordDAO.delete(retrievedRecord.getRecordId());
        assertNull(recordDAO.findById(retrievedRecord.getRecordId()));
    }

    @Test
    void testRelationships() throws SQLException {
        // Create insurance
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-REL-001");
        insurance.setInsuranceProvider("Relationship Test Insurance");
        insuranceDAO.save(insurance);

        // Create patient
        Patient patient = new Patient();
        patient.setName("Relationship Patient");
        patient.setEmail("relationship@example.com");
        patient.setPhone("123-456-7890");
        patientDAO.save(patient);

        // Create doctor
        Doctor doctor = new Doctor();
        doctor.setName("Relationship Doctor");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("DOC789");
        doctorDAO.save(doctor);

        // Create appointment linking patient and doctor
        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setDoctorId(doctor.getDoctorId());
        appointment.setScheduledDateTime(LocalDateTime.now());
        appointment.setStatus("Scheduled");
        appointmentDAO.save(appointment);

        // Create record linked to appointment
        com.chiro.models.Record record = new com.chiro.models.Record();
        record.setAppointmentId(appointment.getAppointmentId());
        record.setVisitDate(LocalDate.now());
        record.setSymptoms("Test symptoms");
        record.setDiagnosis("Test diagnosis");
        recordDAO.save(record);

        // Verify relationships
        Patient retrievedPatient = patientDAO.findById(patient.getPatientId());
        assertNotNull(retrievedPatient);

        List<Appointment> patientAppointments = appointmentDAO.findAll();
        assertTrue(patientAppointments.stream()
                .anyMatch(a -> a.getPatientId().equals(patient.getPatientId()) && 
                             a.getDoctorId().equals(doctor.getDoctorId())));

        List<com.chiro.models.Record> appointmentRecords = recordDAO.findAll();
        assertTrue(appointmentRecords.stream()
                .anyMatch(r -> r.getAppointmentId().equals(appointment.getAppointmentId())));
    }
} 