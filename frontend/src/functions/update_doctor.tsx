import React, { useState } from "react";

const UpdateDoctor: React.FC = () => {
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [specialty, setSpecialty] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`/doctors/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, specialty }),
      });

      if (response.ok) {
        setStatus("Doctor updated successfully.");
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update doctor."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setName("");
    setSpecialty("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input value={id} onChange={(e) => setId(e.target.value)} placeholder="Doctor ID" required />
      <input value={name} onChange={(e) => setName(e.target.value)} placeholder="New Name" />
      <input value={specialty} onChange={(e) => setSpecialty(e.target.value)} placeholder="New Specialty" />
      <button type="submit">Update Doctor</button>
      {status && <p>{status}</p>}
    </form>
  );
};

export default UpdateDoctor;
