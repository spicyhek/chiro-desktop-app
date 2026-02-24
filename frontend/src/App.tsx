import { useState, useEffect } from 'react'
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

type SectionKey = 'appointments' | 'patients' | 'records' | 'doctors'
type ActionKey = 'add' | 'update' | 'delete'

export default function App() {
    const [appointments, setAppointments] = useState<Appointment[]>([])
    const [patients, setPatients]         = useState<Patient[]>([])
    const [doctors, setDoctors]           = useState<Doctor[]>([])
    const [records, setRecords]           = useState<Record[]>([])

    const [activeSection, setActiveSection] = useState<SectionKey>('appointments')
    const [activeAction, setActiveAction]   = useState<ActionKey>('add')

    const refreshAppointments = () => {
        fetch('http://localhost:8080/appointments')
            .then(r => r.json())
            .then(setAppointments)
            .catch(err => console.error('Error reloading appointments:', err))
    }

    const refreshPatients = () => {
        fetch('http://localhost:8080/patients')
            .then(r => r.json())
            .then(setPatients)
            .catch(err => console.error('Error reloading patients:', err))
    }

    const refreshDoctors = () => {
        fetch('http://localhost:8080/doctors')
            .then(r => r.json())
            .then(setDoctors)
            .catch(err => console.error('Error reloading doctors:', err))
    }

    const refreshRecords = () => {
        fetch('http://localhost:8080/records')
            .then(r => r.json())
            .then(setRecords)
            .catch(err => console.error('Error reloading records:', err))
    }

    useEffect(() => { refreshAppointments() }, [])
    useEffect(() => { refreshPatients()     }, [])
    useEffect(() => { refreshDoctors()      }, [])
    useEffect(() => { refreshRecords()      }, [])

    const handleUpdate = () => {
        refreshAppointments()
        refreshPatients()
        refreshDoctors()
        refreshRecords()
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
            case 'medicalRecords':
            case 'records':
                refreshRecords()
                break
            default:
                console.warn('Unknown type to delete:', entityType)
        }
    }

    const getSectionLabel = (section: SectionKey) => {
        switch (section) {
            case 'appointments':
                return 'Appointments'
            case 'patients':
                return 'Patients'
            case 'records':
                return 'Records'
            case 'doctors':
                return 'Doctors'
        }
    }

    const renderActiveTable = () => {
        switch (activeSection) {
            case 'appointments':
                return <Appointments data={appointments} />
            case 'patients':
                return <Patients data={patients} />
            case 'records':
                return <Records data={records} />
            case 'doctors':
                return <Doctors data={doctors} />
        }
    }

    const renderAddForm = () => {
        switch (activeSection) {
            case 'appointments':
                return (
                    <AddAppointmentSection
                        onAdd={(newAppt) => setAppointments(prev => [...prev, newAppt])}
                    />
                )
            case 'patients':
                return (
                    <AddPatientSection
                        onAdd={(newPatient) => setPatients(prev => [...prev, newPatient])}
                    />
                )
            case 'records':
                return (
                    <AddRecordSection
                        onAdd={(newRecord) => setRecords(prev => [...prev, newRecord])}
                    />
                )
            case 'doctors':
                return (
                    <AddDoctorSection
                        onAdd={(newDoctor) => setDoctors(prev => [...prev, newDoctor])}
                    />
                )
        }
    }

    return (
        <div className="app-shell">
            <header className="app-header">
                <div className="app-header-main">
                    <div>
                        <h1 className="app-title">Chiropractor DB</h1>
                        <p className="app-subtitle">Clinic overview and data management</p>
                    </div>
                    <div className="app-header-meta">
                        <span className="app-badge">Backend: :8080</span>
                    </div>
                </div>

                <nav className="section-nav">
                    {(['appointments', 'patients', 'records', 'doctors'] as SectionKey[]).map(
                        (key) => (
                            <button
                                key={key}
                                type="button"
                                className={
                                    'section-pill' +
                                    (activeSection === key ? ' section-pill--active' : '')
                                }
                                onClick={() => setActiveSection(key)}
                            >
                                {getSectionLabel(key)}
                            </button>
                        ),
                    )}
                </nav>
            </header>

            <div className="app-main">
                <aside className="app-sidebar">
                    <p className="sidebar-label">Snapshot</p>
                    <ul className="sidebar-list">
                        <li className="sidebar-item">
                            <span>Appointments</span>
                            <span className="sidebar-count">{appointments.length}</span>
                        </li>
                        <li className="sidebar-item">
                            <span>Patients</span>
                            <span className="sidebar-count">{patients.length}</span>
                        </li>
                        <li className="sidebar-item">
                            <span>Doctors</span>
                            <span className="sidebar-count">{doctors.length}</span>
                        </li>
                        <li className="sidebar-item">
                            <span>Records</span>
                            <span className="sidebar-count">{records.length}</span>
                        </li>
                    </ul>
                </aside>

                <main className="app-content">
                    <section className="panel panel--data">
                        <div className="panel-header">
                            <h2 className="panel-title">{getSectionLabel(activeSection)}</h2>
                            <span className="panel-meta">
                                {activeSection === 'appointments' && `${appointments.length} items`}
                                {activeSection === 'patients' && `${patients.length} items`}
                                {activeSection === 'doctors' && `${doctors.length} items`}
                                {activeSection === 'records' && `${records.length} items`}
                            </span>
                        </div>
                        <div className="panel-body">{renderActiveTable()}</div>
                    </section>

                    <section className="panel panel--manage">
                        <div className="panel-header panel-header--split">
                            <h2 className="panel-title">Manage data</h2>
                            <div className="manage-tabs">
                                {(['add', 'update', 'delete'] as ActionKey[]).map((action) => (
                                    <button
                                        key={action}
                                        type="button"
                                        className={
                                            'manage-tab' +
                                            (activeAction === action ? ' manage-tab--active' : '')
                                        }
                                        onClick={() => setActiveAction(action)}
                                    >
                                        {action === 'add' && 'Add'}
                                        {action === 'update' && 'Update'}
                                        {action === 'delete' && 'Delete'}
                                    </button>
                                ))}
                            </div>
                        </div>
                        <div className="panel-body">
                            {activeAction === 'add' && renderAddForm()}
                            {activeAction === 'update' && <UpdateEntity onUpdate={handleUpdate} />}
                            {activeAction === 'delete' && <DeleteEntity onDelete={handleDelete} />}
                        </div>
                    </section>
                </main>
            </div>
        </div>
    )
}
