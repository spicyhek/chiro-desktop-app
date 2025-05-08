import React, { useState } from "react";

type UpdateRecordProps = {
    onUpdate: () => void;
};

const UpdateRecord: React.FC<UpdateRecordProps> = ({ onUpdate }) => {
  const [id, setId] = useState("");
  const [patientId, setPatientId] = useState("");
  const [visitDate, setVisitDate] = useState("");
  const [symptoms, setSymptoms] = useState("");
  const [diagnosis, setDiagnosis] = useState("");
  const [treatment, setTreatment] = useState("");
  const [notes, setNotes] = useState("");
  const [nextRecommendedVisit, setRecommendedVisit] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
  e.preventDefault();

    const updateData: Record<string, string> = {};
    if (patientId) updateData.patientId = patientId;
    if (visitDate) updateData.visitDate = visitDate;
    if (symptoms) updateData.symptoms = symptoms;
    if (diagnosis) updateData.diagnosis = diagnosis;
    if (treatment) updateData.treatment = treatment;
    if (notes) updateData.notes = notes;
    if (nextRecommendedVisit) updateData.nextRecommendedVisit = nextRecommendedVisit;

    try {
      const response = await fetch(`http://localhost:8080/records/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updateData),
      });
      console.log("Sending update:", updateData);

      if (response.ok) {
        setStatus("Record updated successfully.");
        onUpdate();
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update medicalRecord."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setPatientId("");
    setVisitDate("");
    setSymptoms("");
    setDiagnosis("");
    setTreatment("");
    setNotes("");
    setRecommendedVisit("");
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
         value={patientId}
         onChange={(e) => setPatientId(e.target.value)}
         placeholder="Patient ID"
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
      <input
        type="text"
        value={treatment}
        onChange={(e) => setTreatment(e.target.value)}
        placeholder="Treatment"
      />
      <input
        type="text"
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
        placeholder="Notes"
      />
      <input
        type="date"
        value={nextRecommendedVisit}
        onChange={(e) => setRecommendedVisit(e.target.value)}
        placeholder="Next Recommended Visit"
      />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">
        Update Record
      </button>
      {status && <p className="text-sm mt-2">{status}</p>}
    </form>
  );
};

export default UpdateRecord;
