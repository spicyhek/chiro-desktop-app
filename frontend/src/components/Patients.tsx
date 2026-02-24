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
      <div className="data-grid">
        <div className="data-grid__header">
          <div className="data-grid__cell data-grid__cell--header">ID</div>
          <div className="data-grid__cell data-grid__cell--header">Patient</div>
          <div className="data-grid__cell data-grid__cell--header">Date of Birth</div>
          <div className="data-grid__cell data-grid__cell--header">Email</div>
          <div className="data-grid__cell data-grid__cell--header">Phone</div>
          <div className="data-grid__cell data-grid__cell--header">Insurance ID</div>
          <div className="data-grid__cell data-grid__cell--header">Emergency Contact</div>
          <div className="data-grid__cell data-grid__cell--header">Emergency Phone</div>
        </div>
        <div className="data-grid__body">
          {data.map((pat) => (
            <div className="data-grid__row" key={pat.patientId}>
              <div className="data-grid__cell">{pat.patientId}</div>
              <div className="data-grid__cell">{pat.name}</div>
              <div className="data-grid__cell">
                {new Date(pat.dateOfBirth).toLocaleDateString()}
              </div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {pat.email}
              </div>
              <div className="data-grid__cell">{pat.phone}</div>
              <div className="data-grid__cell">{pat.insuranceId}</div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {pat.emergencyContactName}
              </div>
              <div className="data-grid__cell">{pat.emergencyContactPhone}</div>
            </div>
          ))}
          {data.length === 0 && (
            <div className="data-grid__empty">No patients yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}