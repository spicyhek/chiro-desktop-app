import { useState } from 'react';

type Doctor = {
  doctorId: string;
  name: string;
  specialization: string;
  licenseNumber: string;
};

type Props = {
  onAdd: (doctor: Doctor) => void;
};

export default function AddDoctorSection({ onAdd }: Props) {
  const [newDoctor, setNewDoctor] = useState({
    name: '',
    specialization: '',
    licenseNumber: '',
  });

  const handleAddDoctor = () => {
    fetch('http://localhost:8080/doctors', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newDoctor),
    })
      .then(async (res) => {
        const data = await res.json();
        if (!res.ok) {
          throw new Error(text);
        }


        console.log('Created doctor:', data);
        onAdd(data);
        setNewDoctor({
          name: '',
          specialization: '',
          licenseNumber: '',
        });
      })
      .catch((err) => {
        console.log("Payload:", JSON.stringify(newDoctor));
        console.error('Failed to add doctor:', err.message);
        alert('Failed to add doctor: ' + err.message);
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewDoctor({
      ...newDoctor,
      [name]: value,
    });
  };

  return (
    <div>
      <h3>New Doctor</h3>
      <input
        name="name"
        placeholder="Name"
        value={newDoctor.name}
        onChange={handleInputChange}
        required
      />
      <input
        name="specialization"
        placeholder="Specialization"
        value={newDoctor.specialization}
        onChange={handleInputChange}
      />
      <input
        name="licenseNumber"
        placeholder="License Number"
        value={newDoctor.licenseNumber}
        onChange={handleInputChange}
        required
      />
      <button onClick={handleAddDoctor}>Submit</button>
    </div>
  );
}
