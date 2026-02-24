// Appointments.tsx
type Appointment = {
  appointmentId?: string;
  patientId: string;
  doctorId: string;
  scheduledDateTime: string;
  status: string;
  appointmentNotes?: string;
};

type Props = {
  data: Appointment[];
};

export default function Appointments({ data }: Props) {
  return (
    <div className="appointments-box">
      <div className="data-grid">
        <div className="data-grid__header">
          <div className="data-grid__cell data-grid__cell--header">ID</div>
          <div className="data-grid__cell data-grid__cell--header">Patient</div>
          <div className="data-grid__cell data-grid__cell--header">Doctor</div>
          <div className="data-grid__cell data-grid__cell--header">Scheduled</div>
          <div className="data-grid__cell data-grid__cell--header">Status</div>
          <div className="data-grid__cell data-grid__cell--header">Notes</div>
        </div>
        <div className="data-grid__body">
          {data.map((appt) => (
            <div className="data-grid__row" key={appt.appointmentId ?? `${appt.patientId}-${appt.doctorId}-${appt.scheduledDateTime}`}>
              <div className="data-grid__cell">{appt.appointmentId}</div>
              <div className="data-grid__cell">{appt.patientId}</div>
              <div className="data-grid__cell">{appt.doctorId}</div>
              <div className="data-grid__cell">
                {new Date(appt.scheduledDateTime).toLocaleString()}
              </div>
              <div className="data-grid__cell">{appt.status}</div>
              <div className="data-grid__cell data-grid__cell--truncate">
                {appt.appointmentNotes}
              </div>
            </div>
          ))}
          {data.length === 0 && (
            <div className="data-grid__empty">No appointments yet.</div>
          )}
        </div>
      </div>
    </div>
  );
}
