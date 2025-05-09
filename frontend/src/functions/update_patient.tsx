import React, { useState } from "react";

type UpdatePatientProps = {
    onUpdate: () => void;
};

const UpdatePatient: React.FC<UpdatePatientProps> = ({ onUpdate }) => {
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [dateOfBirth, setDateOfBirth] = useState("");
  const [phone, setPhone] = useState("");
  const [insuranceId, setInsuranceId] = useState("");
  const [emergencyContactName, setEmergencyContactName] = useState("");
  const [emergencyContactPhone, setEmergencyContactPhone] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

  const updateData: Record<string, string> = {};
  if (name) updateData.name = name;
  if (dateOfBirth) updateData.dateOfBirth = dateOfBirth;
  if (phone) updateData.phone = phone;
  if (insuranceId) updateData.insuranceId = insuranceId;
  if (emergencyContactName) updateData.emergencyContactName = emergencyContactName;
  if (emergencyContactPhone) updateData.emergencyContactPhone = emergencyContactPhone;

    try {
      const response = await fetch(`http://localhost:8080/patients/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updateData),
      });

      if (response.ok) {
        setStatus("Patient updated successfully.");
        onUpdate();
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update patient."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setName("");
    setDateOfBirth("");
    setPhone("");
    setInsuranceId("");
    setEmergencyContactName("");
    setEmergencyContactPhone("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input
         type = "text"
         value={id} onChange={(e) => setId(e.target.value)}
         placeholder="Patient ID"
         required
      />
      <input
         type = "text"
         value={name}
         onChange={(e) => setName(e.target.value)}
         placeholder="New Name"
      />
      <input
          type = "date"
          value={dateOfBirth}
          onChange={(e) => setDateOfBirth(e.target.value)}
          placeholder="New DOB (YYYY-MM-DD)"
       />
      <input
          type = "text"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          placeholder="New Phone"
       />
       <input
          type = "text"
          value={insuranceId}
          onChange={(e) => setInsuranceId(e.target.value)}
          placeholder="New Insurance ID"
      />
      <input
          type = "text"
          value={emergencyContactName}
          onChange={(e) => setEmergencyContactName(e.target.value)}
          placeholder="New Contact Name"
      />
      <input
          type = "text"
          value={emergencyContactPhone}
          onChange={(e) => setEmergencyContactPhone(e.target.value)}
          placeholder="New Contact Phone"
      />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">
        Update Patient
      </button>
      {status && <p className="text-sm mt-2">{status}</p>}
    </form>
  );
};

export default UpdatePatient;
