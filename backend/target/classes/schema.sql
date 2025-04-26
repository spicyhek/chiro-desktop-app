-- Create Insurance table
CREATE TABLE IF NOT EXISTS insurance (
    InsuranceID INTEGER PRIMARY KEY AUTOINCREMENT,
    InsuranceProvider TEXT NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Patients table
CREATE TABLE IF NOT EXISTS patients (
    PatientID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name TEXT NOT NULL,
    DateOfBirth DATE NOT NULL,
    Email TEXT,
    PhoneNumber TEXT,
    InsuranceID INTEGER,
    EmergencyContactName TEXT,
    EmergencyContactPhone TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (InsuranceID) REFERENCES insurance(InsuranceID)
);

-- Create Doctors table
CREATE TABLE IF NOT EXISTS doctors (
    DoctorID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name TEXT NOT NULL,
    Email TEXT,
    PhoneNumber TEXT,
    LicenseNumber TEXT NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Appointments table
CREATE TABLE IF NOT EXISTS appointments (
    AppointmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    PatientID INTEGER NOT NULL,
    DoctorID INTEGER NOT NULL,
    ScheduledDateTime TIMESTAMP NOT NULL,
    Status TEXT NOT NULL CHECK (Status IN ('Scheduled', 'Completed', 'Cancelled')),
    AppointmentNotes TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (PatientID) REFERENCES patients(PatientID),
    FOREIGN KEY (DoctorID) REFERENCES doctors(DoctorID)
);

-- Create Records table
CREATE TABLE IF NOT EXISTS records (
    RecordID INTEGER PRIMARY KEY AUTOINCREMENT,
    AppointmentID INTEGER NOT NULL,
    VisitDate DATE NOT NULL,
    Symptoms TEXT,
    Diagnosis TEXT,
    NextVisitRecommendedDate DATE,
    RecordNotes TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AppointmentID) REFERENCES appointments(AppointmentID)
); 