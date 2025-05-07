import React, { useState } from 'react';
import UpdateAppointment from './update_appointment';
import UpdatePatient from './update_patient';
import UpdateDoctor from './update_doctor';
import UpdateRecord from './update_record';

const UpdateEntity: React.FC = () => {
  const [type, setType] = useState("appointments");

  return (
    <div className="update-entity">
     <h3>Update Data</h3>
      <select value={type} onChange={(e) => setType(e.target.value)}>
        <option value="appointments">Appointment</option>
        <option value="patients">Patient</option>
        <option value="doctors">Doctor</option>
        <option value="records">Record</option>
      </select>

      <div className="mt-4">
        {type === "appointments" && <UpdateAppointment />}
        {type === "patients" && <UpdatePatient />}
        {type === "doctors" && <UpdateDoctor />}
        {type === "records" && <UpdateRecord />}
      </div>
    </div>
  );
};

export default UpdateEntity;
