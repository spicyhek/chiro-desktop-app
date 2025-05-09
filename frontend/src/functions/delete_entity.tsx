import React, { useState } from "react";

type DeleteEntityProps = {
  onDelete: (entityType: string, id: string) => void;
};

const API_BASE = "http://localhost:8080";

const DeleteEntity: React.FC<DeleteEntityProps> = ({ onDelete }) => {
  const [entityType, setEntityType] = useState("appointments");
  const [entityId, setEntityId]     = useState("");
  const [status, setStatus]         = useState("");

  const handleDelete = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(
          `${API_BASE}/${entityType}/${entityId}`,
          { method: "DELETE" }
      );

      if (response.ok) {
        setStatus(`${capitalize(entityType)} ${entityId} deleted successfully.`);
        onDelete(entityType, entityId);
      } else {
        const error = await response.json();
        setStatus(`Error: ${error.message || "Failed to delete."}`);
      }
    } catch (err) {
      setStatus("Error: Unable to connect to server.");
    }

    setEntityId("");
  };

  const capitalize = (word: string) =>
      word.charAt(0).toUpperCase() + word.slice(1);

  return (
      <div className="delete-entity">
        <h3>Delete Data</h3>
        <form onSubmit={handleDelete} className="space-y-4">
          <select
              value={entityType}
              onChange={(e) => setEntityType(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded"
          >
            <option value="appointments">Appointment</option>
            <option value="patients">Patient</option>
            <option value="medicalRecords">Record</option>
            <option value="doctors">Doctor</option>
          </select>

          <input
              type="text"
              value={entityId}
              onChange={(e) => setEntityId(e.target.value)}
              placeholder={`Enter ${entityType.slice(0, -1)} ID`}
              className="w-full p-2 border border-gray-300 rounded"
              required
          />

          <button
              type="submit"
              className="w-full bg-red-500 text-white p-2 rounded hover:bg-red-600"
          >
            Delete
          </button>
        </form>

        {status && <p className="mt-2 text-sm text-gray-600">{status}</p>}
      </div>
  );
};

export default DeleteEntity;
