import React, { useState } from 'react';
import UpdateAppointment from './update_appointment';
import UpdatePatient from './update_patient';
import UpdateDoctor from './update_doctor';
import UpdateRecord from './update_record';

type UpdateEntityProps = {
  onUpdate: () => void;
};

const UpdateEntity: React.FC<UpdateEntityProps> = ({ onUpdate }) => {
  const [type, setType] = useState("appointments");

  return (

    <div className="update-entity">
     <h3>Update Data</h3>
      <select value={type} onChange={(e) => setType(e.target.value)}>
        <option value="appointments">Appointment</option>
        <option value="patients">Patient</option>
        <option value="doctors">Doctor</option>
        <option value="medicalRecords">Record</option>
      </select>

      <div className="mt-4">
        {type === "appointments" && <UpdateAppointment />}
        {type === "patients" && <UpdatePatient onUpdate={onUpdate}/>}
        {type === "doctors" && <UpdateDoctor onUpdate={onUpdate} />}
        {type === "medicalRecords" && <UpdateRecord onUpdate={onUpdate}/>}
      </div>
    </div>
  );
};

export default UpdateEntity;
