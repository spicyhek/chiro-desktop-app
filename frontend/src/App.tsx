import { useState, useEffect } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Appointments from './components/Appointments'
import Patients from './components/Patients'
import Records from './components/Records'
import Doctors from './components/Doctors'
import AddAppointmentSection from './functions/add_appointment'
import AddPatientSection from './functions/add_patient'
import AddRecordSection from './functions/add_records'
import AddDoctorSection from './functions/add_doctor'
import DeleteEntity from './functions/delete_entity'
import UpdateEntity from './functions/update_entity'


export default function App() {

  const [showAppointments, setShowAppointments] = useState(false);
  const [showPatients, setShowPatients] = useState(false);
  const [showRecords, setShowRecords] = useState(false);
  const [showDoctors, setShowDoctors] = useState(false);
  const [showAddForm, setShowAddForm] = useState(false);

  const [showAddData, setShowAddData] = useState(false);
  const [showDeleteForm, setShowDeleteForm] = useState(false);
  const [showUpdateForm, setShowUpdateForm] = useState(false);


  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [patients, setPatients] = useState<Patient[]>([]);
  const [doctors, setDoctors] = useState<Doctor[]>([]);

  const [records, setRecords] = useState<Record[]>([]);

  useEffect(() => {
    fetch('http://localhost:8080/appointments')
      .then((res) => res.json())
      .then(setAppointments)
      .catch((err) => console.error("Error loading appointments:", err));
  }, []);

  useEffect(() => {
    fetch('http://localhost:8080/patients')
      .then((res) => res.json())
      .then(setPatients)
      .catch((err) => console.error("Error loading patients:", err));
  }, []);

 useEffect(() => {
   fetch('http://localhost:8080/doctors')
     .then((res) => res.json())
     .then(setDoctors)
     .catch((err) => console.error("Error loading doctors:", err));
 }, []);

 useEffect(() => {
   fetch('http://localhost:8080/records')
     .then((res) => res.json())
     .then(setRecords)
     .catch((err) => console.error("Error loading records:", err));
 }, []);


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

        <button className="button" onClick={() => setShowAddData((prev) => !prev)}>
           {showAddData ? 'Cancel' : 'Add Data'}
        </button>

        <button className="button" onClick={() => setShowUpdateForm((prev) => !prev)}>
            {showUpdateForm ? 'Cancel' : 'Update Data'}
         </button>

        <button className="button" onClick={() => setShowDeleteForm((prev) => !prev)}>
           {showDeleteForm ? 'Cancel' : 'Delete Data'}
         </button>

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
         {showAddData && (
           <>
             <AddAppointmentSection onAdd={(newAppt) => setAppointments(prev => [...prev, newAppt])}/>
             <AddPatientSection onAdd={(newPatient) => setPatients(prev => [...prev, newPatient])} />
             <AddRecordSection onAdd={(newRecord) => setRecords(prev => [...prev, newRecord])}/>
             <AddDoctorSection onAdd={(newDoctor) => setDoctors(prev => [...prev, newDoctor])} />
           </>
         )}
        {showDeleteForm && <DeleteEntity />}
        {showUpdateForm && <UpdateEntity />}
       </div>
      </div>

    </div>
  );
}


