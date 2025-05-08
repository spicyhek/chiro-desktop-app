import { useState } from 'react';

type Record = {
    recordId: string;
    patientId: string;
    doctorId: string;
    appointmentId: string;
    visitDate: string;
    symptoms: string;
    diagnosis: string;
    treatment: string;
    notes?: string;
    nextVisitRecommendedDate?: string;
}

type Props = {
  onAdd: (record: Record) => void;
};

export default function AddRecordSection({ onAdd }: Props) {
  const [newRecord, setNewRecord] = useState({
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
    fetch('http://localhost:8080/records', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newRecord),
    })
      .then(async (res) => {
        const isJson = res.headers.get('content-type')?.includes('application/json');
        const data = isJson ? await res.json() : await res.text();

        if (!res.ok) {
          throw new Error(data);
        }
          console.log('Record added:', data);
          onAdd(data)
          setNewRecord({  // Fixed reset to match medicalRecord fields
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
      })
      .catch((err) => {
      console.error('Add record error:', err);
      alert('Invalid input. Please check the values and try again');
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewRecord({
      ...newRecord,
      [name]: value,
    });
  };

  return (
    <div className="add-record-form">
      <h3>New Record</h3>

      <input
        name = "patientId"
        placeholder="Patient ID"
        value={newRecord.patientId}
        onChange={handleInputChange}
      />
      <input
         name="doctorId"
         placeholder="Doctor ID"
         value={newRecord.doctorId}
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
        placeholder="Enter notes about the visit"
        value={newRecord.notes}
        onChange={handleInputChange}
      />
      <input
         name="nextVisitRecommendedDate"
         type = "date"
         placeholder="Next Visit Recommended Date (Optional)"
         value={newRecord.nextVisitRecommendedDate}
         onChange={handleInputChange}
      />

      <button onClick={handleAddRecord}>Submit</button>
    </div>
  );
}
