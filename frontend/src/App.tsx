import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Appointments from './components/Appointments'
import Patients from './components/Patients'
import Records from './components/Records'
import Doctors from './components/Doctors'

export default function App() {
  const [showAppointments, setShowAppointments] = useState(false);
  const [showPatients, setShowPatients] = useState(false);
  const [showRecords, setShowRecords] = useState(false);
  const [showDoctors, setShowDoctors] = useState(false);

  const appointments = [
    { id: 1, patientName: 'Jane Doe', date: '2025-05-01', time: '10:00 AM' },
    { id: 2, patientName: 'John Smith', date: '2025-05-02', time: '11:30 AM' },
  ];

  const patients = [
      { id: 1, patientName: 'Jane Doe', dateOfBirth: '2002-02-12', email: 'doe@gmail.com', phone: '5081648764'},
      { id: 2, patientName: 'John Smith'},
  ];

  const records = [
       { id: 1, patientName: 'Jane Doe', visitDate: '2002-02-12', symptoms: 'backpain', diagnosis: 'stress'},
   ];

  const doctors = [
          { id: 1, doctorName: 'Bob', specialization: 'backs', licenseNumber: '472'},
  ];

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold">Chiropractor DB</h1>
      <hr className="divider" />

      <div className="heading-row">
       <h2 className="component-heading" onClick={() => setShowAppointments((prev) => !prev)}>Appointments</h2>
       <h2 className="component-heading" onClick={() => setShowPatients((prev) => !prev)}>Patients</h2>
       <h2 className="component-heading" onClick={() => setShowRecords((prev) => !prev)}>Records</h2>
       <h2 className="component-heading" onClick={() => setShowDoctors((prev) => !prev)}>Doctors</h2>

       <div className = "button_container">
        <button className="button"> Add Appointment </button>
        <button className="button">Add Patient</button>
        <button className="button">Delete Record</button>
       </div>

      </div>

      <hr className="divider" />

      <div className = "lower-heading">
       <div className="component-box">
         {showAppointments && <Appointments data={appointments} />}
         {showPatients && <Patients data={patients} />}
         {showRecords && <Records data={records} />}
         {showDoctors && <Doctors data={doctors} />}
       </div>

       <div className="function-box">
       </div>
      </div>

    </div>
  );
}


