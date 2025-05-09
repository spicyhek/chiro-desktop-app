import { useState, useEffect } from 'react'
import './App.css'
import Appointments from './components/Appointments'
import Patients from './components/Patients'
import Records from './components/Records'
import Doctors from './components/Doctors'
import Insurances from './components/Insurances'
import AddAppointmentSection from './functions/add_appointment'
import AddPatientSection from './functions/add_patient'
import AddRecordSection from './functions/add_records'
import AddDoctorSection from './functions/add_doctor'
import AddInsuranceSection from './functions/add_insurance'
import DeleteEntity from './functions/delete_entity'
import UpdateEntity from './functions/update_entity'

export default function App() {
    const [showAppointments, setShowAppointments] = useState(false)
    const [showPatients, setShowPatients]         = useState(false)
    const [showRecords, setShowRecords]           = useState(false)
    const [showDoctors, setShowDoctors]           = useState(false)
    const [showInsurances, setShowInsurances]     = useState(false)
    const [showAddData, setShowAddData]           = useState(false)
    const [showDeleteForm, setShowDeleteForm]     = useState(false)
    const [showUpdateForm, setShowUpdateForm]     = useState(false)

    const [appointments, setAppointments] = useState<Appointment[]>([])
    const [patients, setPatients]         = useState<Patient[]>([])
    const [doctors, setDoctors]           = useState<Doctor[]>([])
    const [records, setRecords]           = useState<Record[]>([])
    const [insurances, setInsurances]     = useState<Insurance[]>([])

    const refreshAppointments = () => {
        fetch('http://localhost:8080/appointments')
            .then(r => r.json())
            .then(setAppointments)
            .catch(err => console.error("Error reloading appointments:", err))
    }

    const refreshPatients = () => {
        fetch('http://localhost:8080/patients')
            .then(r => r.json())
            .then(setPatients)
            .catch(err => console.error("Error reloading patients:", err))
    }

    const refreshDoctors = () => {
        fetch('http://localhost:8080/doctors')
            .then(r => r.json())
            .then(setDoctors)
            .catch(err => console.error("Error reloading doctors:", err))
    }

    const refreshRecords = () => {
        fetch('http://localhost:8080/records')
            .then(r => r.json())
            .then(setRecords)
            .catch(err => console.error("Error reloading records:", err))
    }

    const refreshInsurances = () => {
        fetch('http://localhost:8080/insurances')
            .then(r => r.json())
            .then(setInsurances)
            .catch(err => console.error("Error reloading insurances:", err))
    }

    useEffect(() => { refreshAppointments() }, [])
    useEffect(() => { refreshPatients()     }, [])
    useEffect(() => { refreshDoctors()      }, [])
    useEffect(() => { refreshRecords()      }, [])
    useEffect(() => { refreshInsurances()   }, [])

    const handleUpdate = () => {
        refreshAppointments()
        refreshPatients()
        refreshDoctors()
        refreshRecords()
        refreshInsurances()
    }

    const handleDelete = (entityType: string, id: string) => {
        switch (entityType) {
            case 'appointments':
                refreshAppointments()
                break
            case 'patients':
                refreshPatients()
                break
            case 'doctors':
                refreshDoctors()
                break
            case 'records':
            case 'medicalRecords':
                refreshRecords()
                break
            case 'insurances':
                refreshInsurances()
                break
            default:
                console.warn('Unknown type to delete:', entityType)
        }
    }

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold">Chiropractor DB</h1>
            <hr className="divider" />

            <div className="heading-row">
                <h2 className="component-heading" onClick={() => setShowAppointments(prev => !prev)}>Appointments</h2>
                <h2 className="component-heading" onClick={() => setShowPatients(prev => !prev)}>Patients</h2>
                <h2 className="component-heading" onClick={() => setShowRecords(prev => !prev)}>Records</h2>
                <h2 className="component-heading" onClick={() => setShowDoctors(prev => !prev)}>Doctors</h2>
                <h2 className="component-heading" onClick={() => setShowInsurances(prev => !prev)}>Insurances</h2>

                <div className="button_container">
                    <button className="button" onClick={() => setShowAddData(prev => !prev)}>
                        {showAddData ? 'Cancel' : 'Add Data'}
                    </button>

                    <button className="button" onClick={() => setShowUpdateForm(prev => !prev)}>
                        {showUpdateForm ? 'Cancel' : 'Update Data'}
                    </button>

                    <button className="button" onClick={() => setShowDeleteForm(prev => !prev)}>
                        {showDeleteForm ? 'Cancel' : 'Delete Data'}
                    </button>
                </div>
            </div>

            <hr className="divider" />

            <div className="lower-heading">
                <div className="component-box">
                    {showAppointments && <Appointments data={appointments} />}
                    {showPatients     && <Patients     data={patients}     />}
                    {showRecords      && <Records      data={records}      />}
                    {showDoctors      && <Doctors      data={doctors}      />}
                    {showInsurances   && <Insurances   data={insurances}   />}
                </div>

                <div className="function-box">
                    {showAddData && (
                        <>
                            <AddAppointmentSection onAdd={newA => setAppointments(prev => [...prev, newA])} />
                            <AddPatientSection     onAdd={newP => setPatients(prev => [...prev, newP])} />
                            <AddRecordSection      onAdd={newR => setRecords(prev => [...prev, newR])} />
                            <AddDoctorSection      onAdd={newD => setDoctors(prev => [...prev, newD])} />
                            <AddInsuranceSection   onAdd={newI => setInsurances(prev => [...prev, newI])} />
                        </>
                    )}

                    {showDeleteForm && <DeleteEntity onDelete={handleDelete} />}
                    {showUpdateForm && <UpdateEntity onUpdate={handleUpdate} />}
                </div>
            </div>

        </div>
    )
}
