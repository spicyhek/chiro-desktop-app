// UpdateAppointment.tsx
import React, { useState } from 'react';

const UpdateAppointment: React.FC = () => {
  const [id, setId] = useState("");
  const [date, setDate] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`/appointments/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ date }), // include other fields as needed
      });

      if (response.ok) {
        setStatus("Appointment updated.");
      } else {
        setStatus("Failed to update appointment.");
      }
    } catch {
      setStatus("Server error.");
    }
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input value={id} onChange={(e) => setId(e.target.value)} placeholder="Appointment ID" />
      <input value={date} onChange={(e) => setDate(e.target.value)} placeholder="New Date" />
      <button type="submit">Update</button>
      {status && <p>{status}</p>}
    </form>
  );
};

export default UpdateAppointment;
