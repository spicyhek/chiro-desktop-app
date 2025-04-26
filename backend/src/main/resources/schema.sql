-- Create Doctor table
CREATE TABLE IF NOT EXISTS Doctor (
    doctorId INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    specialization TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Patient table
CREATE TABLE IF NOT EXISTS Patient (
    patientId INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    dateOfBirth DATE,
    address TEXT,
    phone TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Appointment table
CREATE TABLE IF NOT EXISTS Appointment (
    appointmentId INTEGER PRIMARY KEY AUTOINCREMENT,
    patientId INTEGER,
    doctorId INTEGER,
    appointmentDate TIMESTAMP NOT NULL,
    status TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patientId) REFERENCES Patient(patientId),
    FOREIGN KEY (doctorId) REFERENCES Doctor(doctorId)
);

-- Create Record table
CREATE TABLE IF NOT EXISTS Record (
    recordId INTEGER PRIMARY KEY AUTOINCREMENT,
    appointmentId INTEGER,
    visitDate DATE NOT NULL,
    symptoms TEXT,
    diagnosis TEXT,
    nextVisitRecommendedDate DATE,
    recordNotes TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointmentId) REFERENCES Appointment(appointmentId)
);

-- Create Insurance table
CREATE TABLE IF NOT EXISTS Insurance (
    insuranceId INTEGER PRIMARY KEY AUTOINCREMENT,
    insuranceProvider TEXT NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 