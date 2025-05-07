import React, { useState } from "react";

const UpdateRecord: React.FC = () => {
  const [id, setId] = useState("");
  const [patientName, setPatientName] = useState("");
  const [visitDate, setVisitDate] = useState("");
  const [symptoms, setSymptoms] = useState("");
  const [diagnosis, setDiagnosis] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`/records/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ patientName, visitDate, symptoms, diagnosis }),
      });

      if (response.ok) {
        setStatus("Record updated successfully.");
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update medicalRecord."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setPatientName("");
    setVisitDate("");
    setSymptoms("");
    setDiagnosis("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input
        type="text"
        value={id}
        onChange={(e) => setId(e.target.value)}
        placeholder="Record ID"
        required
      />
      <input
        type="text"
        value={patientName}
        onChange={(e) => setPatientName(e.target.value)}
        placeholder="Patient Name"
      />
      <input
        type="date"
        value={visitDate}
        onChange={(e) => setVisitDate(e.target.value)}
        placeholder="Visit Date"
      />
      <input
        type="text"
        value={symptoms}
        onChange={(e) => setSymptoms(e.target.value)}
        placeholder="Symptoms"
      />
      <input
        type="text"
        value={diagnosis}
        onChange={(e) => setDiagnosis(e.target.value)}
        placeholder="Diagnosis"
      />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">
        Update Record
      </button>
      {status && <p className="text-sm mt-2">{status}</p>}
    </form>
  );
};

export default UpdateRecord;
