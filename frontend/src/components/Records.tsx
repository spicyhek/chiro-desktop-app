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
  data: Record[];
};

export default function Records({ data }: Props) {
  return (
    <div className="records-box">
        <h2>Records</h2>
      <table className="components-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>ID</th>
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
              <td>{rec.recordId}</td>
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