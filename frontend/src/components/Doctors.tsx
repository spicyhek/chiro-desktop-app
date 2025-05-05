type Doctor = {
  id: number;
  doctorName: string;
  specialization: string;
  licenseNumber: string;
};

type Props = {
  data: Doctor[];
};

export default function Doctors({ data }: Props) {
  return (
    <div className="doctors-box">
        <h2>Doctors</h2>
      <table className="components-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Specialization</th>
            <th>licenseNumber</th>
          </tr>
        </thead>
        <tbody>
          {data.map((doc) => (
            <tr key={doc.id}>
              <td>{doc.doctorName}</td>
              <td>{doc.specialization}</td>
              <td>{doc.licenseNumber}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
