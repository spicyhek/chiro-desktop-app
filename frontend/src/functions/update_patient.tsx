import React, { useState } from "react";

const UpdatePatient: React.FC = () => {
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [dob, setDob] = useState("");
  const [phone, setPhone] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`/patients/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, dob, phone }),
      });

      if (response.ok) {
        setStatus("Patient updated successfully.");
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update patient."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setName("");
    setDob("");
    setPhone("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input value={id} onChange={(e) => setId(e.target.value)} placeholder="Patient ID" required />
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="New Name" />
      <input value={dob} onChange={(e) => setDob(e.target.value)} placeholder="New DOB (YYYY-MM-DD)" />
      <input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="New Phone" />
      <button type="submit">Update Patient</button>
      {status && <p>{status}</p>}
    </form>
  );
};

export default UpdatePatient;
