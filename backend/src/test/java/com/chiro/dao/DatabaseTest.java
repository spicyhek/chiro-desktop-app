package com.chiro.dao;

import com.chiro.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DatabaseIntegrationTest {

    @Autowired PatientDAO patientDAO;
    @Autowired DoctorDAO doctorDAO;
    @Autowired InsuranceDAO insuranceDAO;
    @Autowired AppointmentDAO appointmentDAO;
    @Autowired RecordDAO recordDAO;
    @Autowired DataSource dataSource;

    @BeforeAll
    void recreateSchema() throws Exception {
        String ddl = new String(Files.readAllBytes(Paths.get("src/main/resources/schema.sql")));
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : ddl.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }
    

    @BeforeEach
    void cleanTables() throws Exception {
        try (Connection c = dataSource.getConnection();
             Statement s = c.createStatement()) {

            s.execute("PRAGMA foreign_keys = OFF");
            for (String table : new String[]{
                    "Record", "Appointment", "Insurance", "Patient", "Doctor"
            }) {
                s.execute("DELETE FROM " + table);
            }
            s.execute("PRAGMA foreign_keys = ON");
        }
    }

    @Test
    void insuranceCrud() throws Exception {
        Insurance ins = new Insurance();
        ins.setInsuranceId("INS-1");
        ins.setInsuranceProvider("TestCo");
        insuranceDAO.save(ins);

        Insurance fetched = insuranceDAO.findById("INS-1");
        assertEquals("TestCo", fetched.getInsuranceProvider());

        fetched.setInsuranceProvider("NewCo");
        insuranceDAO.save(fetched);
        assertEquals("NewCo", insuranceDAO.findById("INS-1").getInsuranceProvider());

        insuranceDAO.delete("INS-1");
        assertNull(insuranceDAO.findById("INS-1"));
    }

    @Test
    void patientCrud() throws Exception {
        Insurance ins = new Insurance();
        ins.setInsuranceId("INS-P");
        ins.setInsuranceProvider("PCo");
        insuranceDAO.save(ins);

        Patient p = new Patient();
        p.setName("Alice");
        p.setDateOfBirth(LocalDate.of(1980, 1, 1));
        p.setEmail("a@ex.com");
        p.setPhone("555");
        p.setInsuranceId("INS-P");
        p.setEmergencyContactName("Bob");
        p.setEmergencyContactPhone("666");
        patientDAO.save(p);
        assertNotNull(p.getPatientId());

        Patient fetched = patientDAO.findById(p.getPatientId());
        assertEquals("Alice", fetched.getName());

        fetched.setName("Alice2");
        patientDAO.save(fetched);
        assertEquals("Alice2", patientDAO.findById(fetched.getPatientId()).getName());

        patientDAO.delete(fetched.getPatientId());
        assertNull(patientDAO.findById(fetched.getPatientId()));
    }

    @Test
    void doctorCrud() throws Exception {
        Doctor d = new Doctor();
        d.setName("Dr Who");
        d.setSpecialization("Time");
        d.setLicenseNumber("XYZ");
        doctorDAO.save(d);
        assertNotNull(d.getDoctorId());

        Doctor fetched = doctorDAO.findById(d.getDoctorId());
        assertEquals("Dr Who", fetched.getName());

        fetched.setSpecialization("Space");
        doctorDAO.save(fetched);
        assertEquals("Space", doctorDAO.findById(fetched.getDoctorId()).getSpecialization());

        doctorDAO.delete(fetched.getDoctorId());
        assertNull(doctorDAO.findById(fetched.getDoctorId()));
    }

    @Test
    void appointmentCrud() throws Exception {
        Patient p = new Patient();
        p.setName("P");
        patientDAO.save(p);
        Doctor d = new Doctor();
        d.setName("D");
        d.setSpecialization("Spec");
        d.setLicenseNumber("L");
        doctorDAO.save(d);

        Appointment a = new Appointment();
        a.setPatientId(p.getPatientId());
        a.setDoctorId(d.getDoctorId());
        a.setScheduledDateTime(LocalDateTime.now().plusDays(1));
        a.setStatus("Scheduled");
        appointmentDAO.save(a);
        assertNotNull(a.getAppointmentId());

        Appointment fetched = appointmentDAO.findById(a.getAppointmentId());
        assertEquals("Scheduled", fetched.getStatus());

        fetched.setStatus("Done");
        appointmentDAO.save(fetched);
        assertEquals("Done", appointmentDAO.findById(fetched.getAppointmentId()).getStatus());

        appointmentDAO.delete(fetched.getAppointmentId());
        assertNull(appointmentDAO.findById(fetched.getAppointmentId()));
    }

    @Test
    void recordCrud() throws Exception {
        Patient p = new Patient();
        p.setName("P");
        patientDAO.save(p);
        Doctor d = new Doctor();
        d.setName("D");
        d.setSpecialization("S");
        d.setLicenseNumber("L");
        doctorDAO.save(d);
        Appointment a = new Appointment();
        a.setPatientId(p.getPatientId());
        a.setDoctorId(d.getDoctorId());
        a.setScheduledDateTime(LocalDateTime.now());
        a.setStatus("Done");
        appointmentDAO.save(a);

        MedicalRecord r = new MedicalRecord();
        r.setAppointmentId(a.getAppointmentId());
        r.setVisitDate(LocalDate.now());
        r.setSymptoms("S");
        r.setDiagnosis("D");
        recordDAO.save(r);
        assertNotNull(r.getRecordId());

        MedicalRecord fetched = recordDAO.findById(r.getRecordId());
        assertEquals("S", fetched.getSymptoms());

        fetched.setDiagnosis("Dx2");
        recordDAO.save(fetched);
        assertEquals("Dx2", recordDAO.findById(fetched.getRecordId()).getDiagnosis());

        recordDAO.delete(fetched.getRecordId());
        assertNull(recordDAO.findById(fetched.getRecordId()));
    }

    @Test
    void relationshipsAreIntact() throws Exception {
        Insurance ins = new Insurance();
        ins.setInsuranceId("I");
        ins.setInsuranceProvider("X");
        insuranceDAO.save(ins);

        Patient p = new Patient();
        p.setName("Px");
        p.setInsuranceId("I");
        patientDAO.save(p);

        Doctor d = new Doctor();
        d.setName("Dx");
        d.setSpecialization("S");
        d.setLicenseNumber("L");
        doctorDAO.save(d);

        Appointment a = new Appointment();
        a.setPatientId(p.getPatientId());
        a.setDoctorId(d.getDoctorId());
        a.setScheduledDateTime(LocalDateTime.now());
        a.setStatus("S");
        appointmentDAO.save(a);

        MedicalRecord r = new MedicalRecord();
        r.setAppointmentId(a.getAppointmentId());
        r.setVisitDate(LocalDate.now());
        r.setSymptoms("Sym");
        r.setDiagnosis("Dia");
        recordDAO.save(r);

        List<Appointment> apps = appointmentDAO.findAll();
        assertTrue(apps.stream()
                .anyMatch(x -> x.getPatientId().equals(p.getPatientId())));
        List<MedicalRecord> recs = recordDAO.findAll();
        assertTrue(recs.stream()
                .anyMatch(x -> x.getAppointmentId().equals(a.getAppointmentId())));
    }
}
