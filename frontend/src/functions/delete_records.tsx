import { useState } from 'react';

export default function DeleteRecordsSection() {
    const [delRecord, setDelRecord] = useState({
        appointmentID: '',
        patientID: '',
        patientName: '',
        doctorId: '',
        scheduledDateTime: '',
        status: '',
        appointmentNotes: '',
    });

}