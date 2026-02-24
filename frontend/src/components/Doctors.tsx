type Doctor = {
  doctorId: number;
  name: string;
  specialization: string;
  licenseNumber: string;
};

type Props = {
  data: Doctor[];
};

export default function Doctors({ data }: Props) {
  return (
    <div className="doctors-box">
      <div className="data-grid">
        <div className="data-grid__header">
          <div className="data-grid__cell data-grid__cell--header">Doctor ID</div>
          <div className="data-grid__cell data-grid__cell--header">Name</div>
          <div className="data-grid__cell data-grid__cell--header">Specialization</div>
          <div className="data-grid__cell data-grid__cell--header">License #</div>
        </div>
        <div className="data-grid__body">
          {data.map((doc) => (
            <div className="data-grid__row" key={doc.doctorId}>
              <div className="data-grid__cell">{doc.doctorId}</div>
              <div className="data-grid__cell">{doc.name}</div>
              <div className="data-grid__cell">{doc.specialization}</div>
              <div className="data-grid__cell">{doc.licenseNumber}</div>
            </div>
          ))}
          {data.length === 0 && (
            <div className="data-grid__empty">No doctors yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}
