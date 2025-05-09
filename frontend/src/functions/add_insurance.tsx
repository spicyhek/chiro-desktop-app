import React, { useState } from 'react'
import { Insurance } from '../components/Insurances'

type Props = {
    onAdd: (insurance: Insurance) => void
}

export default function AddInsuranceSection({ onAdd }: Props) {
    const [provider, setProvider] = useState('')

    const handleAddInsurance = async () => {
        try {
            const res = await fetch('http://localhost:8080/insurances', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ insuranceProvider: provider }),
            })
            if (!res.ok) {
                const text = await res.text()
                throw new Error(text)
            }
            const created: Insurance = await res.json()
            onAdd(created)
            setProvider('')
        } catch (err: any) {
            console.error('Failed to add insurance:', err.message)
            alert('Failed to add insurance: ' + err.message)
        }
    }

    return (
        <div className="add-insurance-form">
            <h3>New Insurance</h3>
            <input
                name="insuranceProvider"
                placeholder="Insurance Provider"
                value={provider}
                onChange={(e) => setProvider(e.target.value)}
                required
                className="w-full p-2 border border-gray-300 rounded"
            />
            <button
                onClick={handleAddInsurance}
                className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600 mt-2"
            >
                Submit
            </button>
        </div>
    )
}
