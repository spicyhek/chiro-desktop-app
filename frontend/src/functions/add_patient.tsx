import { useState } from 'react';

type Patient = {
  patientId: string;
  name: string;
  dateOfBirth: string;
  email?: string;
  phone?: string;
  insuranceId?: number;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
};

type Props = {
  onAdd: (patient: Patient) => void;
};


export default function AddPatientSection({ onAdd }: Props) {
  const [newPatient, setNewPatient] = useState({
    name: '',
    dateOfBirth: '',
    email: '',
    phone: '',
    insuranceId: null,
    emergencyContactName: '',
    emergencyContactPhone: '',
  });


  const handleAddPatient = () => {
    fetch('http://localhost:8080/patients', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newPatient),
    })
      .then(async (res) => {
        const text = await res.text();
        if (!res.ok) {
          throw new Error(text);
        }
        const created = JSON.parse(text);
        console.log("Created patient:", created);
        onAdd(created);
        setNewPatient({
          name: '',
          dateOfBirth: '',
          email: '',
          phone: '',
          insuranceId: null,
          emergencyContactName: '',
          emergencyContactPhone: '',
        });
      })
      .catch((err) => {
        console.error('Failed to add patient:', err.message);
        alert('Failed to add patient: ' + err.message);
      });
  };


  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewPatient({
      ...newPatient,
      [name]: value,
    });
  };

  return (
    <div className="add-patient-form">
      <h3>New Patient</h3>

      <input
        name="name"  // Matches backend field (not 'patientName')
        placeholder="Full Name"
        value={newPatient.name}
        onChange={handleInputChange}
        required
      />
      <input
        name="dateOfBirth"
        type="date"
        placeholder="Date of Birth"
        value={newPatient.dateOfBirth}
        onChange={handleInputChange}
        required
      />
      <input
        name="email"
        type="email"
        placeholder="Email"
        value={newPatient.email}
        onChange={handleInputChange}
      />
      <input
        name="phone"
        placeholder="Phone Number"
        value={newPatient.phone}
        onChange={handleInputChange}
      />
      <input
        name="insuranceId"
        type="number"
        placeholder="Insurance ID (optional)"
        value={newPatient.insuranceId || ''}
        onChange={handleInputChange}
      />
      <input
        name="emergencyContactName"
        placeholder="Emergency Contact Name"
        value={newPatient.emergencyContactName}
        onChange={handleInputChange}
      />
      <input
        name="emergencyContactPhone"
        placeholder="Emergency Contact Phone"
        value={newPatient.emergencyContactPhone}
        onChange={handleInputChange}
      />

      <button onClick={handleAddPatient}>Submit</button>
    </div>
  );
}
