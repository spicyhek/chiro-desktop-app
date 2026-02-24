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
      <div className="data-grid data-grid--wide">
        <div className="data-grid__header">
          <div className="data-grid__cell data-grid__cell--header">ID</div>
          <div className="data-grid__cell data-grid__cell--header">Patient ID</div>
          <div className="data-grid__cell data-grid__cell--header">Visit Date</div>
          <div className="data-grid__cell data-grid__cell--header">Symptoms</div>
          <div className="data-grid__cell data-grid__cell--header">Diagnosis</div>
          <div className="data-grid__cell data-grid__cell--header">Treatment</div>
          <div className="data-grid__cell data-grid__cell--header">Notes</div>
          <div className="data-grid__cell data-grid__cell--header">
            Next Visit Recommended Date
          </div>
        </div>
        <div className="data-grid__body">
          {data.map((rec) => (
            <div className="data-grid__row" key={rec.recordId}>
              <div className="data-grid__cell">{rec.recordId}</div>
              <div className="data-grid__cell">{rec.patientId}</div>
              <div className="data-grid__cell">{rec.visitDate}</div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {rec.symptoms}
              </div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {rec.diagnosis}
              </div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {rec.treatment}
              </div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {rec.notes}
              </div>
              <div className="data-grid__cell">{rec.nextVisitRecommendedDate}</div>
            </div>
          ))}
          {data.length === 0 && (
            <div className="data-grid__empty">No records yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}