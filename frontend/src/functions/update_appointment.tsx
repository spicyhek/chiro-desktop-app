// UpdateAppointment.tsx
import React, { useState } from 'react';

type UpdateAppointmentProps = {
    onUpdate: () => void;
};

const UpdateAppointment: React.FC<UpdateAppointmentProps> = ({ onUpdate }) => {
  const [id, setId] = useState("");
  const [patientId, setPatientId] = useState("");
  const [doctorId, setDoctorId] = useState("");
  const [scheduledDateTime, setScheduledDateTime] = useState("");
  const [status, setStatus] = useState("");
  const [appointmentNotes, setAppointmentNotes] = useState("");
  const [message, setMessage] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    const updateData: Record<string, string> = {};
    if (patientId) updateData.patientId = patientId;
    if (doctorId) updateData.doctorId = doctorId;
    if (scheduledDateTime) updateData.scheduledDateTime = scheduledDateTime;
    if (status) updateData.status = status;
    if (appointmentNotes) updateData.appointmentNotes = appointmentNotes;


    try {
      const response = await fetch(`http://localhost:8080/appointments/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateData), // include other fields as needed
      });

      if (response.ok) {
          setMessage("Appointment updated.");
          onUpdate();
        } else {
          const error = await response.json();
          setMessage(`Error: ${error.message || "Failed to update doctor."}`);
        }
      } catch {
        setStatus("Error: Unable to reach the server.");
      }

    setDoctorId("");
    setPatientId("");
    setScheduledDateTime("");
    setStatus("");
    setAppointmentNotes("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input
         type="text"
         value={id}
         onChange={(e) => setId(e.target.value)}
         placeholder="Appointment ID"
         required
      />
      <input
         type="text"
         value={patientId}
         onChange={(e) => setPatientId(e.target.value)}
         placeholder="New Patient Id"
       />
       <input
         type="text"
         value={doctorId}
         onChange={(e) => setDoctorId(e.target.value)}
         placeholder="New Doctor Id"
       />
       <input
         type="datetime-local"
         value={scheduledDateTime}
         onChange={(e) => setScheduledDateTime(e.target.value)}
         placeholder="New Scheduled Date"
       />
       <select
         value={status}
         onChange={(e) => setStatus(e.target.value)}
         className="border p-2 rounded w-full"
       >
         <option value="">Select Status</option>
         <option value="Scheduled">Scheduled</option>
         <option value="Completed">Completed</option>
         <option value="Cancelled">Cancelled</option>
       </select>

       <input
         type="text"
         value={appointmentNotes}
         onChange={(e) => setAppointmentNotes(e.target.value)}
         placeholder="New Notes"
       />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">
         Update Appointment
      </button>
      {message && <p className="text-sm mt-2">{message}</p>}
    </form>
  );
};

export default UpdateAppointment;
