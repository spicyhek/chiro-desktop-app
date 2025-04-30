import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold">Chiropractor DB</h1>
      <hr className="divider" />

      <div className="heading-row">
       <h2 className="appointments-heading">Appointments</h2>
       <h2 className="patients-heading">Patients</h2>
       <h2 className="records-heading">Records</h2>
       <h2 className="doctors-heading">Doctors</h2>

      </div>

      <hr className="divider" />

      <div className="boxes-container">
       <div className="schedule-box">
         <h2 className="schedule-heading">Today's Schedule:</h2>
       </div>

       <div className="recent-box">
           <h2 className="recent-heading">Recent Activity:</h2>
       </div>

      </div>

    </div>
  );
}

export default App;

