import React, { useState } from 'react';

type UpdateInsuranceProps = {
    onUpdate: () => void;
};

const UpdateInsurance: React.FC<UpdateInsuranceProps> = ({ onUpdate }) => {
    const [id, setId] = useState('');
    const [provider, setProvider] = useState('');
    const [status, setStatus] = useState('');

    const handleUpdate = async (e: React.FormEvent) => {
        e.preventDefault();

        const updateData: Record<string, string> = {};
        if (provider) updateData.insuranceProvider = provider;

        try {
            const response = await fetch(
                `http://localhost:8080/insurances/${id}`,
                {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(updateData),
                }
            );

            if (response.ok) {
                setStatus('Insurance updated successfully.');
                onUpdate();
            } else {
                const err = await response.text();
                setStatus(`Error: ${err || 'Failed to update insurance.'}`);
            }
        } catch {
            setStatus('Error: Unable to reach the server.');
        }

        setId('');
        setProvider('');
    };

    return (
        <form onSubmit={handleUpdate} className="space-y-4">
            <input
                type="text"
                value={id}
                onChange={e => setId(e.target.value)}
                placeholder="Insurance ID"
                required
                className="w-full p-2 border rounded"
            />
            <input
                type="text"
                value={provider}
                onChange={e => setProvider(e.target.value)}
                placeholder="New Insurance Provider"
                required
                className="w-full p-2 border rounded"
            />
            <button
                type="submit"
                className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600"
            >
                Update Insurance
            </button>
            {status && <p className="mt-2 text-sm text-gray-200">{status}</p>}
        </form>
    );
};

export default UpdateInsurance;
