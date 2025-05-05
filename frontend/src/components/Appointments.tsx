// Appointments.tsx
type Appointment = {
  id: number;
  patientName: string;
  date: string;
  time: string;
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
            <th>Patient</th>
            <th>Date</th>
            <th>Time</th>
          </tr>
        </thead>
        <tbody>
          {data.map((appt) => (
            <tr key={appt.id}>
              <td>{appt.patientName}</td>
              <td>{appt.date}</td>
              <td>{appt.time}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
