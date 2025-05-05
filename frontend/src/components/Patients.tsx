type Patient = {
  id: number;
  patientName: string;
  dateOfBirth: localDate;
  email: string;
  phone: string;
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
            <th>Patient</th>
            <th>Date of Birth</th>
            <th>Email</th>
            <th>Phone</th>
          </tr>
        </thead>
        <tbody>
          {data.map((pat) => (
            <tr key={pat.id}>
              <td>{pat.patientName}</td>
              <td>{pat.dateOfBirth}</td>
              <td>{pat.email}</td>
              <td>{pat.phone}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}