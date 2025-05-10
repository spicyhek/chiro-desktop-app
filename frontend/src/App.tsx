// src/App.tsx
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

interface Appointment {
    appointmentId: string
    patientId:     string
    doctorId:      string
    scheduledDateTime: string
    status:        string
    appointmentNotes?: string
}

interface Patient {
    patientId: string
    name:      string
    dateOfBirth: string
    email?:    string
    phone?:    string
    insuranceId?: string
    emergencyContactName?: string
    emergencyContactPhone?: string
}

interface MedicalRecord {
    recordId: string
    appointmentId: string
    visitDate: string
    symptoms: string
    diagnosis: string
    nextVisitRecommendedDate?: string
    recordNotes?: string
}

interface Doctor {
    doctorId: string
    name:     string
    email:    string
    phone:    string
    licenseNumber: string
}

interface Insurance {
    insuranceId:      string
    insuranceProvider:string
}


const API = 'http://localhost:8080'

export default function App() {
    const [showAppointments, setShowAppointments] = useState(false)
    const [showPatients,     setShowPatients]     = useState(false)
    const [showRecords,      setShowRecords]      = useState(false)
    const [showDoctors,      setShowDoctors]      = useState(false)
    const [showInsurances,   setShowInsurances]   = useState(false)

    const [appointments, setAppointments] = useState<Appointment[]>([])
    const [patients,     setPatients]     = useState<Patient[]>([])
    const [records,      setRecords]      = useState<MedicalRecord[]>([])
    const [doctors,      setDoctors]      = useState<Doctor[]>([])
    const [insurances,   setInsurances]   = useState<Insurance[]>([])

    const [showAddData,   setShowAddData]   = useState(false)
    const [showDeleteForm,setShowDeleteForm]= useState(false)
    const [showUpdateForm,setShowUpdateForm]= useState(false)

    // Search input state
    const [apptQuery,    setApptQuery]    = useState('')
    const [patientQuery, setPatientQuery] = useState('')
    const [recordQuery,  setRecordQuery]  = useState('')
    const [doctorQuery,  setDoctorQuery]  = useState('')
    const [insQuery,     setInsQuery]     = useState('')

    const refreshAppointments = () =>
        fetch(`${API}/appointments`)
            .then(r => r.json())
            .then(setAppointments)
            .catch(console.error)

    const refreshPatients = () =>
        fetch(`${API}/patients`)
            .then(r => r.json())
            .then(setPatients)
            .catch(console.error)

    const refreshRecords = () =>
        fetch(`${API}/records`)
            .then(r => r.json())
            .then(setRecords)
            .catch(console.error)

    const refreshDoctors = () =>
        fetch(`${API}/doctors`)
            .then(r => r.json())
            .then(setDoctors)
            .catch(console.error)

    const refreshInsurances = () =>
        fetch(`${API}/insurances`)
            .then(r => r.json())
            .then(setInsurances)
            .catch(console.error)


    const searchAppointments = (q: string) =>
        fetch(`${API}/appointments/search?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(setAppointments)
            .catch(console.error)

    const searchPatients = (q: string) =>
        fetch(`${API}/patients/search?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(setPatients)
            .catch(console.error)

    const searchRecords = (q: string) =>
        fetch(`${API}/records/search?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(setRecords)
            .catch(console.error)

    const searchDoctors = (q: string) =>
        fetch(`${API}/doctors/search?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(setDoctors)
            .catch(console.error)

    const searchInsurances = (q: string) =>
        fetch(`${API}/insurances/search?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(setInsurances)
            .catch(console.error)

    useEffect(() => { refreshAppointments() }, [])
    useEffect(() => { refreshPatients()     }, [])
    useEffect(() => { refreshRecords()      }, [])
    useEffect(() => { refreshDoctors()      }, [])
    useEffect(() => { refreshInsurances()   }, [])

    const handleUpdate = () => {
        refreshAppointments()
        refreshPatients()
        refreshRecords()
        refreshDoctors()
        refreshInsurances()
    }

    const handleDelete = (type: string) => {
        switch (type) {
            case 'appointments': refreshAppointments(); break
            case 'patients':     refreshPatients();     break
            case 'records':      refreshRecords();      break
            case 'doctors':      refreshDoctors();      break
            case 'insurances':   refreshInsurances();   break
            default: console.warn('Unknown delete type:', type)
        }
    }

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold">Chiropractor DB</h1>
            <hr className="divider" />

            <div className="heading-row">
                <h2 className="component-heading" onClick={() => setShowAppointments(v => !v)}>Appointments</h2>
                <h2 className="component-heading" onClick={() => setShowPatients(v => !v)}>Patients</h2>
                <h2 className="component-heading" onClick={() => setShowRecords(v => !v)}>Records</h2>
                <h2 className="component-heading" onClick={() => setShowDoctors(v => !v)}>Doctors</h2>
                <h2 className="component-heading" onClick={() => setShowInsurances(v => !v)}>Insurances</h2>

                <div className="button_container">
                    <button className="button" onClick={() => setShowAddData(v => !v)}>
                        {showAddData ? 'Cancel' : 'Add Data'}
                    </button>
                    <button className="button" onClick={() => setShowUpdateForm(v => !v)}>
                        {showUpdateForm ? 'Cancel' : 'Update Data'}
                    </button>
                    <button className="button" onClick={() => setShowDeleteForm(v => !v)}>
                        {showDeleteForm ? 'Cancel' : 'Delete Data'}
                    </button>
                </div>
            </div>

            <hr className="divider" />

            <div className="lower-heading">
                <div className="component-box">

                    {showAppointments && (
                        <div>
                            {/* Appointments Search */}
                            <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                                <input
                                    type="text"
                                    value={apptQuery}
                                    onChange={e => setApptQuery(e.target.value)}
                                    placeholder="Search appointments…"
                                    style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
                                />
                                <button onClick={() => searchAppointments(apptQuery)}>Search</button>
                                <button onClick={() => { setApptQuery(''); refreshAppointments() }}>Reset</button>
                            </div>
                            <Appointments data={appointments} />
                        </div>
                    )}

                    {showPatients && (
                        <div>
                            {/* Patients Search */}
                            <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                                <input
                                    type="text"
                                    value={patientQuery}
                                    onChange={e => setPatientQuery(e.target.value)}
                                    placeholder="Search patients…"
                                    style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
                                />
                                <button onClick={() => searchPatients(patientQuery)}>Search</button>
                                <button onClick={() => { setPatientQuery(''); refreshPatients() }}>Reset</button>
                            </div>
                            <Patients data={patients} />
                        </div>
                    )}

                    {showRecords && (
                        <div>
                            {/* Records Search */}
                            <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                                <input
                                    type="text"
                                    value={recordQuery}
                                    onChange={e => setRecordQuery(e.target.value)}
                                    placeholder="Search records…"
                                    style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
                                />
                                <button onClick={() => searchRecords(recordQuery)}>Search</button>
                                <button onClick={() => { setRecordQuery(''); refreshRecords() }}>Reset</button>
                            </div>
                            <Records data={records} />
                        </div>
                    )}

                    {showDoctors && (
                        <div>
                            {/* Doctors Search */}
                            <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                                <input
                                    type="text"
                                    value={doctorQuery}
                                    onChange={e => setDoctorQuery(e.target.value)}
                                    placeholder="Search doctors…"
                                    style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
                                />
                                <button onClick={() => searchDoctors(doctorQuery)}>Search</button>
                                <button onClick={() => { setDoctorQuery(''); refreshDoctors() }}>Reset</button>
                            </div>
                            <Doctors data={doctors} />
                        </div>
                    )}

                    {showInsurances && (
                        <div>
                            {/* Insurances Search */}
                            <div style={{ display: 'flex', gap: 8, marginBottom: 12 }}>
                                <input
                                    type="text"
                                    value={insQuery}
                                    onChange={e => setInsQuery(e.target.value)}
                                    placeholder="Search insurances…"
                                    style={{ flex: 1, padding: 8, border: '1px solid #ccc', borderRadius: 4 }}
                                />
                                <button onClick={() => searchInsurances(insQuery)}>Search</button>
                                <button onClick={() => { setInsQuery(''); refreshInsurances() }}>Reset</button>
                            </div>
                            <Insurances data={insurances} />
                        </div>
                    )}

                </div>

                <div className="function-box">
                    {showAddData && (
                        <>
                            <AddAppointmentSection onAdd={newA => setAppointments(prev => [newA, ...prev])} />
                            <AddPatientSection     onAdd={newP => setPatients(prev => [newP, ...prev])} />
                            <AddRecordSection      onAdd={newR => setRecords(prev => [newR, ...prev])} />
                            <AddDoctorSection      onAdd={newD => setDoctors(prev => [newD, ...prev])} />
                            <AddInsuranceSection   onAdd={newI => setInsurances(prev => [newI, ...prev])} />
                        </>
                    )}
                    {showDeleteForm && <DeleteEntity onDelete={handleDelete} />}
                    {showUpdateForm && <UpdateEntity onUpdate={handleUpdate} />}
                </div>
            </div>
        </div>
    )
}
