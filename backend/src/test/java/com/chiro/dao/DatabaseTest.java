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
        System.out.println("\n=== Setting up test environment ===");
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
        System.out.println("=== Test environment setup complete ===\n");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("\n=== Cleaning up test environment ===");
        // Delete the test database file
        try {
            java.nio.file.Files.deleteIfExists(java.nio.file.Path.of("test.db"));
            System.out.println("Test database file deleted");
        } catch (Exception e) {
            System.out.println("Error during cleanup: " + e.getMessage());
        }
        System.out.println("=== Cleanup complete ===\n");
    }

    @BeforeEach
    void clearDatabase() throws SQLException {
        System.out.println("\n--- Clearing database for new test ---");
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
                    System.out.println("Cleared table: " + table);
                } catch (SQLException e) {
                    // If table doesn't exist, initialize database and try again
                    if (e.getMessage().contains("no such table")) {
                        System.out.println("Table " + table + " not found, reinitializing database");
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
        System.out.println("--- Database cleared ---\n");
    }

    @Test
    void testInsuranceOperations() throws SQLException {
        System.out.println("\n=== Testing Insurance Operations ===");
        
        // Create insurance
        System.out.println("Creating new insurance record...");
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS123");
        insurance.setInsuranceProvider("Test Insurance Co");
        insuranceDAO.save(insurance);
        System.out.println("Created insurance with ID: " + insurance.getInsuranceId());
        assertNotNull(insurance.getInsuranceId());
        assertEquals("INS123", insurance.getInsuranceId());

        // Read insurance
        System.out.println("Reading insurance record...");
        Insurance retrievedInsurance = insuranceDAO.findById(insurance.getInsuranceId());
        assertNotNull(retrievedInsurance);
        assertEquals("Test Insurance Co", retrievedInsurance.getInsuranceProvider());
        System.out.println("Retrieved insurance provider: " + retrievedInsurance.getInsuranceProvider());

        // Update insurance
        System.out.println("Updating insurance record...");
        retrievedInsurance.setInsuranceProvider("Updated Insurance Co");
        insuranceDAO.save(retrievedInsurance);
        Insurance updatedInsurance = insuranceDAO.findById(retrievedInsurance.getInsuranceId());
        assertEquals("Updated Insurance Co", updatedInsurance.getInsuranceProvider());
        System.out.println("Updated insurance provider: " + updatedInsurance.getInsuranceProvider());

        // Delete insurance
        System.out.println("Deleting insurance record...");
        insuranceDAO.delete(retrievedInsurance.getInsuranceId());
        assertNull(insuranceDAO.findById(retrievedInsurance.getInsuranceId()));
        System.out.println("Insurance record deleted successfully");
        
        System.out.println("=== Insurance Operations Test Complete ===\n");
    }

    @Test
    void testInsuranceIdRequired() {
        System.out.println("\n=== Testing Insurance ID Requirement ===");
        Insurance insurance = new Insurance();
        insurance.setInsuranceProvider("Test Insurance Co");
        
        System.out.println("Attempting to save insurance without ID...");
        assertThrows(IllegalArgumentException.class, () -> {
            insuranceDAO.save(insurance);
        });
        System.out.println("Expected exception thrown: Insurance ID is required");
        System.out.println("=== Insurance ID Requirement Test Complete ===\n");
    }

    @Test
    void testPatientOperations() throws SQLException {
        System.out.println("\n=== Testing Patient Operations ===");
        
        // Create insurance first (required for patient)
        System.out.println("Creating insurance for patient...");
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-PAT-001");
        insurance.setInsuranceProvider("Patient Test Insurance");
        insuranceDAO.save(insurance);
        System.out.println("Created insurance with ID: " + insurance.getInsuranceId());

        // Create patient
        System.out.println("Creating new patient record...");
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setEmail("john@example.com");
        patient.setPhone("123-456-7890");
        patient.setEmergencyContactName("Jane Doe");
        patient.setEmergencyContactPhone("987-654-3210");
        patientDAO.save(patient);
        System.out.println("Created patient with ID: " + patient.getPatientId());
        assertNotNull(patient.getPatientId());
        assertTrue(patient.getPatientId().length() > 0);

        // Read patient
        System.out.println("Reading patient record...");
        Patient retrievedPatient = patientDAO.findById(patient.getPatientId());
        assertNotNull(retrievedPatient);
        assertEquals("John Doe", retrievedPatient.getName());
        System.out.println("Retrieved patient name: " + retrievedPatient.getName());

        // Update patient
        System.out.println("Updating patient record...");
        retrievedPatient.setName("John Smith");
        patientDAO.save(retrievedPatient);
        Patient updatedPatient = patientDAO.findById(retrievedPatient.getPatientId());
        assertEquals("John Smith", updatedPatient.getName());
        System.out.println("Updated patient name: " + updatedPatient.getName());

        // Delete patient
        System.out.println("Deleting patient record...");
        patientDAO.delete(retrievedPatient.getPatientId());
        assertNull(patientDAO.findById(retrievedPatient.getPatientId()));
        System.out.println("Patient record deleted successfully");
        
        System.out.println("=== Patient Operations Test Complete ===\n");
    }

    @Test
    void testDoctorOperations() throws SQLException {
        System.out.println("\n=== Testing Doctor Operations ===");
        
        // Create doctor
        System.out.println("Creating new doctor record...");
        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");
        doctor.setSpecialization("Chiropractic");
        doctor.setLicenseNumber("CHIRO123");
        doctorDAO.save(doctor);
        System.out.println("Created doctor with ID: " + doctor.getDoctorId());
        assertNotNull(doctor.getDoctorId());
        assertTrue(doctor.getDoctorId().length() > 0);

        // Read doctor
        System.out.println("Reading doctor record...");
        Doctor retrievedDoctor = doctorDAO.findById(doctor.getDoctorId());
        assertNotNull(retrievedDoctor);
        assertEquals("Dr. Smith", retrievedDoctor.getName());
        assertEquals("Chiropractic", retrievedDoctor.getSpecialization());
        System.out.println("Retrieved doctor: " + retrievedDoctor.getName() + " - " + retrievedDoctor.getSpecialization());

        // Update doctor
        System.out.println("Updating doctor record...");
        retrievedDoctor.setSpecialization("Sports Medicine");
        doctorDAO.save(retrievedDoctor);
        Doctor updatedDoctor = doctorDAO.findById(retrievedDoctor.getDoctorId());
        assertEquals("Sports Medicine", updatedDoctor.getSpecialization());
        System.out.println("Updated doctor specialization: " + updatedDoctor.getSpecialization());

        // Delete doctor
        System.out.println("Deleting doctor record...");
        doctorDAO.delete(retrievedDoctor.getDoctorId());
        assertNull(doctorDAO.findById(retrievedDoctor.getDoctorId()));
        System.out.println("Doctor record deleted successfully");
        
        System.out.println("=== Doctor Operations Test Complete ===\n");
    }

    @Test
    void testAppointmentOperations() throws SQLException {
        System.out.println("\n=== Testing Appointment Operations ===");
        
        // Create required entities first
        System.out.println("Creating required entities...");
        Insurance insurance = new Insurance();
        insurance.setInsuranceId("INS-APT-001");
        insurance.setInsuranceProvider("Appointment Test Insurance");
        insuranceDAO.save(insurance);
        System.out.println("Created insurance with ID: " + insurance.getInsuranceId());

        Patient patient = new Patient();
        patient.setName("Appointment Patient");
        patient.setEmail("appointment@example.com");
        patient.setPhone("123-456-7890");
        patientDAO.save(patient);
        System.out.println("Created patient with ID: " + patient.getPatientId());

        Doctor doctor = new Doctor();
        doctor.setName("Dr. Appointment");
        doctor.setSpecialization("General");
        doctor.setLicenseNumber("APT123");
        doctorDAO.save(doctor);
        System.out.println("Created doctor with ID: " + doctor.getDoctorId());

        // Create appointment
        System.out.println("Creating new appointment...");
        Appointment appointment = new Appointment();
        appointment.setPatientId(patient.getPatientId());
        appointment.setDoctorId(doctor.getDoctorId());
        appointment.setScheduledDateTime(LocalDateTime.now().plusDays(1));
        appointment.setStatus("Scheduled");
        appointmentDAO.save(appointment);
        System.out.println("Created appointment with ID: " + appointment.getAppointmentId());
        assertNotNull(appointment.getAppointmentId());
        assertTrue(appointment.getAppointmentId().length() > 0);

        // Read appointment
        System.out.println("Reading appointment record...");
        Appointment retrievedAppointment = appointmentDAO.findById(appointment.getAppointmentId());
        assertNotNull(retrievedAppointment);
        assertEquals("Scheduled", retrievedAppointment.getStatus());
        System.out.println("Retrieved appointment status: " + retrievedAppointment.getStatus());

        // Update appointment
        System.out.println("Updating appointment record...");
        retrievedAppointment.setStatus("Completed");
        appointmentDAO.save(retrievedAppointment);
        Appointment updatedAppointment = appointmentDAO.findById(retrievedAppointment.getAppointmentId());
        assertEquals("Completed", updatedAppointment.getStatus());
        System.out.println("Updated appointment status: " + updatedAppointment.getStatus());

        // Delete appointment
        System.out.println("Deleting appointment record...");
        appointmentDAO.delete(retrievedAppointment.getAppointmentId());
        assertNull(appointmentDAO.findById(retrievedAppointment.getAppointmentId()));
        System.out.println("Appointment record deleted successfully");
        
        System.out.println("=== Appointment Operations Test Complete ===\n");
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