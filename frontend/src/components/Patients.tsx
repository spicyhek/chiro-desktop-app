type Patient = {
  patientId: number;
  name: string;
  dateOfBirth: string;
  email?: string;
  phone?: string;
  insuranceId?: number;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
};

type Props = {
  data: Patient[];
};

export default function Patients({ data }: Props) {
  return (
    <div className="patients-box">
        <h2>Patients</h2>
      <table className="components-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Patient</th>
            <th>Date of Birth</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Insurance ID</th>
            <th>Emergency Contact</th>
            <th>Emergency Contact Phone</th>
          </tr>
        </thead>
        <tbody>
          {data.map((pat) => (
            <tr key={pat.id}>
              <td>{pat.patientId}</td>
              <td>{pat.name}</td>
              <td>{new Date(pat.dateOfBirth).toLocaleDateString()}</td>
              <td>{pat.email}</td>
              <td>{pat.phone}</td>
              <td>{pat.insuranceId}</td>
              <td>{pat.emergencyContactName}</td>
              <td>{pat.emergencyContactPhone}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}