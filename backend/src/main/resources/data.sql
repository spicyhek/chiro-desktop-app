-- Seed Insurance (15 rows)
INSERT INTO Insurance (insuranceId, insuranceProvider) VALUES
                                                           ('INS-01', 'HealthNet'),
                                                           ('INS-02', 'WellCare'),
                                                           ('INS-03', 'MediPlus'),
                                                           ('INS-04', 'CareFirst'),
                                                           ('INS-05', 'UnitedHealth'),
                                                           ('INS-06', 'BlueShield'),
                                                           ('INS-07', 'Kaiser'),
                                                           ('INS-08', 'Cigna'),
                                                           ('INS-09', 'Aetna'),
                                                           ('INS-10', 'Anthem'),
                                                           ('INS-11', 'AmeriHealth'),
                                                           ('INS-12', 'Molina'),
                                                           ('INS-13', 'Oscar'),
                                                           ('INS-14', 'Humana'),
                                                           ('INS-15', 'Tricare');

-- Seed Doctor (15 rows)
INSERT INTO Doctor (doctorId, name, specialization, licenseNumber) VALUES
                                                                       ('DOC-01', 'Dr. Allen',     'Spine',            'LIC-001'),
                                                                       ('DOC-02', 'Dr. Baker',     'Neurology',        'LIC-002'),
                                                                       ('DOC-03', 'Dr. Chen',      'Orthopedics',      'LIC-003'),
                                                                       ('DOC-04', 'Dr. Diaz',      'Pediatrics',       'LIC-004'),
                                                                       ('DOC-05', 'Dr. Evans',     'Geriatrics',       'LIC-005'),
                                                                       ('DOC-06', 'Dr. Fisher',    'Sports Med',       'LIC-006'),
                                                                       ('DOC-07', 'Dr. Garcia',    'Rehab',            'LIC-007'),
                                                                       ('DOC-08', 'Dr. Hernandez', 'Chiropractic',     'LIC-008'),
                                                                       ('DOC-09', 'Dr. Ivanov',    'Pain Management',  'LIC-009'),
                                                                       ('DOC-10', 'Dr. Jackson',   'Neurology',        'LIC-010'),
                                                                       ('DOC-11', 'Dr. Kim',       'Family Practice',  'LIC-011'),
                                                                       ('DOC-12', 'Dr. Lopez',     'Osteopathy',       'LIC-012'),
                                                                       ('DOC-13', 'Dr. Moore',     'Manual Therapy',   'LIC-013'),
                                                                       ('DOC-14', 'Dr. Nguyen',    'Spine & Joint',    'LIC-014'),
                                                                       ('DOC-15', 'Dr. O''Brien',  'Physical Therapy', 'LIC-015');

-- Seed Patient (15 rows)
INSERT INTO Patient (
    patientId, name, dateOfBirth, email, phone,
    emergencyContactName, emergencyContactPhone, insuranceId
) VALUES
      ('PAT-01', 'Alice Smith',       '1990-01-01', 'alice@example.com',    '555-1001', 'Bob Smith',      '555-2001', 'INS-01'),
      ('PAT-02', 'Ben Johnson',       '1985-02-14', 'ben@example.com',      '555-1002', 'Cathy Johnson',  '555-2002', 'INS-02'),
      ('PAT-03', 'Cindy Wu',          '1978-03-10', 'cindy@example.com',    '555-1003', 'Derek Wu',       '555-2003', 'INS-03'),
      ('PAT-04', 'David Lee',         '1992-04-05', 'david@example.com',    '555-1004', 'Emily Lee',      '555-2004', 'INS-04'),
      ('PAT-05', 'Eve Patel',         '1988-05-22', 'eve@example.com',      '555-1005', 'Frank Patel',    '555-2005', 'INS-05'),
      ('PAT-06', 'George Kim',        '1995-06-12', 'george@example.com',   '555-1006', 'Hana Kim',       '555-2006', 'INS-06'),
      ('PAT-07', 'Helen Martinez',    '1980-07-30', 'helen@example.com',    '555-1007', 'Isaac Martinez', '555-2007', 'INS-07'),
      ('PAT-08', 'Ivan Novak',        '1975-08-09', 'ivan@example.com',     '555-1008', 'Jelena Novak',   '555-2008', 'INS-08'),
      ('PAT-09', 'Jessica Brown',     '1993-09-11', 'jessica@example.com',  '555-1009', 'Kevin Brown',    '555-2009', 'INS-09'),
      ('PAT-10', 'Kevin Zhang',       '1986-10-25', 'kevin@example.com',    '555-1010', 'Laura Zhang',    '555-2010', 'INS-10'),
      ('PAT-11', 'Linda Tran',        '1991-11-18', 'linda@example.com',    '555-1011', 'Mike Tran',      '555-2011', 'INS-11'),
      ('PAT-12', 'Michael Davis',     '1983-12-22', 'mike@example.com',     '555-1012', 'Nancy Davis',    '555-2012', 'INS-12'),
      ('PAT-13', 'Nina Park',         '1996-01-17', 'nina@example.com',     '555-1013', 'Oscar Park',     '555-2013', 'INS-13'),
      ('PAT-14', 'Oliver King',       '1987-02-28', 'oliver@example.com',   '555-1014', 'Paula King',     '555-2014', 'INS-14'),
      ('PAT-15', 'Patricia Reed',     '1979-03-31', 'patricia@example.com', '555-1015', 'Quincy Reed',    '555-2015', 'INS-15');

-- Seed Appointment (15 rows)
INSERT INTO Appointment (
    appointmentId, patientId, doctorId, scheduledDateTime, status, appointmentNotes
) VALUES
      ('APP-01', 'PAT-01', 'DOC-01', '2025-06-01 10:00:00', 'Scheduled',   NULL),
      ('APP-02', 'PAT-02', 'DOC-02', '2025-06-02 11:15:00', 'Completed',   'Follow-up ok'),
      ('APP-03', 'PAT-03', 'DOC-03', '2025-06-03 12:30:00', 'Scheduled',   NULL),
      ('APP-04', 'PAT-04', 'DOC-04', '2025-06-04 13:45:00', 'Cancelled',   'Patient no-show'),
      ('APP-05', 'PAT-05', 'DOC-05', '2025-06-05 14:00:00', 'Scheduled',   NULL),
      ('APP-06', 'PAT-06', 'DOC-06', '2025-06-06 15:15:00', 'Completed',   'Improved mobility'),
      ('APP-07', 'PAT-07', 'DOC-07', '2025-06-07 09:00:00', 'Scheduled',   NULL),
      ('APP-08', 'PAT-08', 'DOC-08', '2025-06-08 10:30:00', 'Scheduled',   NULL),
      ('APP-09', 'PAT-09', 'DOC-09', '2025-06-09 11:30:00', 'Scheduled',   NULL),
      ('APP-10', 'PAT-10', 'DOC-10', '2025-06-10 12:00:00', 'Scheduled',   NULL),
      ('APP-11', 'PAT-11', 'DOC-11', '2025-06-11 13:30:00', 'Completed',   'No pain reported'),
      ('APP-12', 'PAT-12', 'DOC-12', '2025-06-12 14:45:00', 'Scheduled',   NULL),
      ('APP-13', 'PAT-13', 'DOC-13', '2025-06-13 15:30:00', 'Scheduled',   NULL),
      ('APP-14', 'PAT-14', 'DOC-14', '2025-06-14 09:15:00', 'Scheduled',   NULL),
      ('APP-15', 'PAT-15', 'DOC-15', '2025-06-15 10:00:00', 'Scheduled',   NULL);

-- Seed Record (15 rows)
INSERT INTO Record (
    recordId, patientId, doctorId, appointmentId,
    visitDate, symptoms, diagnosis, treatment, notes, nextVisitRecommendedDate
) VALUES
      ('REC-01', 'PAT-01', 'DOC-01', 'APP-01', '2025-06-01', 'Back pain',       'Herniated disc',     'PT',          'Responding well',   '2025-07-01'),
      ('REC-02', 'PAT-02', 'DOC-02', 'APP-02', '2025-06-02', 'Neck stiffness',  'Muscle strain',      'Massage',     'Needs posture work','2025-07-02'),
      ('REC-03', 'PAT-03', 'DOC-03', 'APP-03', '2025-06-03', 'Knee pain',       'Ligament tear',      'Brace',       'Consider surgery',  '2025-07-03'),
      ('REC-04', 'PAT-04', 'DOC-04', 'APP-04', '2025-06-04', 'Shoulder ache',   'Tendonitis',         'Ice/rest',    'Follow-up in 2w',   '2025-07-04'),
      ('REC-05', 'PAT-05', 'DOC-05', 'APP-05', '2025-06-05', 'Sciatic pain',    'Sciatica',           'Stretching',  'Improvement seen',  '2025-07-05'),
      ('REC-06', 'PAT-06', 'DOC-06', 'APP-06', '2025-06-06', 'Hip stiffness',   'Arthritis',          'Medication',  'Manageable',        '2025-07-06'),
      ('REC-07', 'PAT-07', 'DOC-07', 'APP-07', '2025-06-07', 'Spinal tightness','Scoliosis',          'Adjustments','Regular sessions','2025-07-07'),
      ('REC-08', 'PAT-08', 'DOC-08', 'APP-08', '2025-06-08', 'Joint inflammation','Arthritis flare',   'Rest',        'Return in 1w',     '2025-07-08'),
      ('REC-09', 'PAT-09', 'DOC-09', 'APP-09', '2025-06-09', 'Whiplash pain',   'Whiplash',           'Heat therapy','Slow progress',    '2025-07-09'),
      ('REC-10', 'PAT-10', 'DOC-10', 'APP-10', '2025-06-10', 'Lower back ache', 'Bulging disc',       'Chiro+PT',    'Improving',        '2025-07-10'),
      ('REC-11', 'PAT-11', 'DOC-11', 'APP-11', '2025-06-11', 'Frozen shoulder', 'Adhesive capsulitis','Stretching',  'Stiffness reduced','2025-07-11'),
      ('REC-12', 'PAT-12', 'DOC-12', 'APP-12', '2025-06-12', 'Wrist pain',      'Repetitive strain',  'Ergonomics',  'Setup adjusted',   '2025-07-12'),
      ('REC-13', 'PAT-13', 'DOC-13', 'APP-13', '2025-06-13', 'Ankle sprain',    'Sprain grade II',    'Immobilize', 'Follow-up needed', '2025-07-13'),
      ('REC-14', 'PAT-14', 'DOC-14', 'APP-14', '2025-06-14', 'Leg nerve pain',  'Nerve compression',  'Chiro',       'Ongoing',          '2025-07-14'),
      ('REC-15', 'PAT-15', 'DOC-15', 'APP-15', '2025-06-15', 'Heel pain',       'Plantar fasciitis',  'Orthotics',   'Recheck in 1m',    '2025-07-15');
