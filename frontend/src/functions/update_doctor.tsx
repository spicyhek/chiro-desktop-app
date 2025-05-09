import React, { useState } from "react";

type UpdateDoctorProps = {
    onUpdate: () => void;
}

const UpdateDoctor: React.FC<UpdateDoctorProps> = ({ onUpdate }) => {
  const [id, setId] = useState("");
  const [name, setName] = useState("");
  const [specialization, setSpecialization] = useState("");
  const [licenseNumber, setLicenseNumber] = useState("");
  const [status, setStatus] = useState("");

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    const updateData: Record<string, string> = {};

    if (name) updateData.name = name;
    if (specialization) updateData.specialization = specialization;
    if (licenseNumber) updateData.licenseNumber = licenseNumber;

    try {
      const response = await fetch(`http://localhost:8080/doctors/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updateData),
      });

      if (response.ok) {
        setStatus("Doctor updated successfully.");
        onUpdate();
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to update doctor."}`);
      }
    } catch {
      setStatus("Error: Unable to reach the server.");
    }

    setId("");
    setName("");
    setSpecialization("");
    setLicenseNumber("");
  };

  return (
    <form onSubmit={handleUpdate} className="space-y-4">
      <input
         type="text"
         value={id}
         onChange={(e) => setId(e.target.value)}
         placeholder="Doctor ID"
         required
      />
      <input
         type="text"
         value={name}
         onChange={(e) => setName(e.target.value)}
         placeholder="New Name"
      />
      <input
         type="text"
         value={specialization}
         onChange={(e) => setSpecialization(e.target.value)}
         placeholder="New Specialization"
      />
      <input
          type="text"
          value={licenseNumber}
          onChange={(e) => setLicenseNumber(e.target.value)}
          placeholder="New License Number"
       />
      <button type="submit" className="bg-blue-500 text-white p-2 rounded">
         Update Doctor
      </button>
      {status && <p className="text-sm mt-2">{status}</p>}
    </form>
  );
};

export default UpdateDoctor;
