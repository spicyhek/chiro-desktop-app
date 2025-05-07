import { useState } from 'react';

type Appointment = {
  appointmentId?: string;
  patientId: string;
  doctorId: string;
  scheduledDateTime: string;
  status: string;
  appointmentNotes?: string;
};

type Props = {
  onAdd: (appt: Appointment) => void;
};

export default function AddAppointmentSection({ onAdd }: Props) {
  const [newAppointment, setNewAppointment] = useState({
    patientId: '',
    doctorId: '',
    scheduledDateTime: '',
    status: '',
    appointmentNotes: '',
  });

const handleAddAppointment = () => {
  fetch('http://localhost:8080/appointments', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(newAppointment),
  })
    .then(async (res) => {
      const text = await res.text();

      if (!res.ok) {
        console.error('Backend error:', text);
        alert(`Failed to add appointment: ${text}`);
        return;
      }
      const created = JSON.parse(text);
      onAdd(created);
      alert(`Appointment added! ID: ${created.appointmentId}`);
      setNewAppointment({
        patientId: '',
        doctorId: '',
        scheduledDateTime: '',
        status: '',
        appointmentNotes: '',
      });
    })
    .catch((err) => {
      console.error('Failed to add appointment:', err.message);
      alert('Failed to add appointment: ' + err.message);
    });
};
  const handleInputChange = (e) => {
    const { name, value } = e.target;
      setNewAppointment({
        ...newAppointment,
        [name]: value,
    });
  };

  return (
     <div className="add-appointment-form">
          <h3>New Appointment</h3>

          <input
            name ="patientId"
            placeholder="Patient ID"
            value={newAppointment.patientId}
            onChange={handleInputChange}
          />
          <input
            name = "doctorId"
            placeholder="Doctor ID"
            value={newAppointment.doctorId}
            onChange={handleInputChange}
          />
          <input
            name = "scheduledDateTime"
            type="datetime-local"
            placeholder="Scheduled Date/Time"
            value={newAppointment.scheduledDateTime}
            onChange={handleInputChange}
          />
          <select name="status" value={newAppointment.status} onChange={handleInputChange} required>
            <option value="">Select status</option>
            <option value="Scheduled">Scheduled</option>
          </select>
          <input
            name = "appointmentNotes"
            placeholder="Notes"
            value={newAppointment.appointmentNotes}
            onChange={handleInputChange}
          />

          <button onClick={handleAddAppointment}>Submit</button>
        </div>
      );
    }