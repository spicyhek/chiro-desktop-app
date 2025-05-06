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
        <h2>Appointments</h2>
      <table className="components-table" border={1} cellPadding={8}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Patient</th>
            <th>Doctor</th>
            <th>Scheduled</th>
            <th>Status</th>
            <th>Notes</th>
          </tr>
        </thead>
        <tbody>
          {data.map((appt) => (
            <tr key={appt.appointmentId}>
              <td>{appt.appointmentId}</td>
              <td>{appt.patientId}</td>
              <td>{appt.doctorId}</td>
              <td>{new Date(appt.scheduledDateTime).toLocaleString()}</td>
              <td>{appt.status}</td>
              <td>{appt.appointmentNotes}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
