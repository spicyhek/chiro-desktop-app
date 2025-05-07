type Record = {
  id: number;
  patientName: string;
  visitDate: LocalDate;
  symptoms: string;
  diagnosis: string;
  treatment: string;
  notes: string;
  nextVisitRecommendedDate: LocalDate;
};

type Props = {
  data: Record[];
};

export default function Records({ data }: Props) {
  return (
    <div className="medicalRecords-box">
        <h2>Records</h2>
      <table className="components-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Visit Date</th>
            <th>Symptoms</th>
            <th>Diagnosis</th>
            <th>Treatment</th>
            <th>Notes</th>
            <th>Next Visit Recommended Date</th>
          </tr>
        </thead>
        <tbody>
          {data.map((rec) => (
            <tr key={rec.id}>
              <td>{rec.patientName}</td>
              <td>{rec.visitDate}</td>
              <td>{rec.symptoms}</td>
              <td>{rec.diagnosis}</td>
              <td>{rec.treatment}</td>
              <td>{rec.notes}</td>
              <td>{rec.nextVisitRecommendedDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}