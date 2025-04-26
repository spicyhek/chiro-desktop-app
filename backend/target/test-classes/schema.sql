-- Create Doctor table
CREATE TABLE IF NOT EXISTS Doctor (
    doctorId TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    specialization TEXT,
    licenseNumber TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Patient table
CREATE TABLE IF NOT EXISTS Patient (
    patientId TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    dateOfBirth DATE,
    email TEXT,
    phone TEXT,
    emergencyContactName TEXT,
    emergencyContactPhone TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Appointment table
CREATE TABLE IF NOT EXISTS Appointment (
    appointmentId TEXT PRIMARY KEY,
    patientId TEXT,
    doctorId TEXT,
    scheduledDateTime TIMESTAMP NOT NULL,
    status TEXT,
    appointmentNotes TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patientId) REFERENCES Patient(patientId),
    FOREIGN KEY (doctorId) REFERENCES Doctor(doctorId)
);

-- Create Record table
CREATE TABLE IF NOT EXISTS Record (
    recordId TEXT PRIMARY KEY,
    patientId TEXT,
    doctorId TEXT,
    appointmentId TEXT,
    visitDate DATE NOT NULL,
    symptoms TEXT,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    nextVisitRecommendedDate DATE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointmentId) REFERENCES Appointment(appointmentId),
    FOREIGN KEY (patientId) REFERENCES Patient(patientId),
    FOREIGN KEY (doctorId) REFERENCES Doctor(doctorId)
);

-- Create Insurance table
CREATE TABLE IF NOT EXISTS Insurance (
    insuranceId TEXT PRIMARY KEY,
    insuranceProvider TEXT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 