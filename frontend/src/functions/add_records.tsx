import { useState } from 'react';

export default function AddRecordSection() {

  const [newRecord, setNewRecord] = useState({
    recordId: '',
    patientId: '',
    doctorId: '',
    appointmentId: '',
    visitDate: '',
    symptoms: '',
    diagnosis: '',
    treatment: '',
    notes: '',
    nextVisitRecommendedDate: '',
  });

  const handleAddRecord = () => {
    fetch('http://localhost:8080/api/records', {  // Changed endpoint
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newRecord),  // Fixed to use newRecord
    })
      .then((res) => {
        if (res.ok) {
          alert('Record added!');
          setNewRecord({  // Fixed reset to match medicalRecord fields
            recordId: '',
            patientId: '',
            doctorId: '',
            appointmentId: '',
            visitDate: '',
            symptoms: '',
            diagnosis: '',
            treatment: '',
            notes: '',
            nextVisitRecommendedDate: '',
          });
        } else {
          alert('Failed to add medicalRecord.');
        }
      })
      .catch(() => alert('Server error — check backend.'));
  };


  const handleInputChange = (e) => {
    const { name, value } = e.target;
       setNewRecord({
         ...newRecord,
         [name]: value,
    });
  };

  return (
    <div className="add-medicalRecord-form">
      <h3>New Record</h3>

      <input
        name = "recordId"
        placeholder="Record ID"
        value={newRecord.recordId}
        onChange={handleInputChange}
      />
      <input
        name = "patientId"
        placeholder="Patient ID"
        value={newRecord.recordId}
        onChange={handleInputChange}
      />
      <input
         name="doctorId"
         placeholder="Doctor ID"
         value={newRecord.recordId}
         onChange={handleInputChange}
      />
      <input
        name="appointmentId"
        placeholder="Appointment ID"
        value={newRecord.appointmentId}
        onChange={handleInputChange}
      />
       <input
         name="visitDate"
         type="date"
         placeholder="Visit Date"
         value={newRecord.visitDate}
         onChange={handleInputChange}
       />
       <input
         name="symptoms"
         placeholder="Symptoms"
         value={newRecord.symptoms}
         onChange={handleInputChange}
       />
       <input
         name="diagnosis"
         placeholder="Diagnosis"
         value={newRecord.diagnosis}
         onChange={handleInputChange}
       />
      <input
        name="treatment"
        placeholder="Treatment"
        value={newRecord.treatment}
        onChange={handleInputChange}
      />
      <input
        name="notes"
        placeholder="Notes"
        value={newRecord.notes}
        onChange={handleInputChange}
      />
      <input
         name="nextVisitRecommendedDate"
         type = "date"
         placeholder="Next Visit Recommended Date"
         value={newRecord.nextVisitRecommendedDate}
         onChange={handleInputChange}
      />

      <button onClick={handleAddRecord}>Submit</button>
    </div>
  );
}
