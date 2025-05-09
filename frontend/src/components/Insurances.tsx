export type Insurance = {
    insuranceId: number;
    insuranceProvider: string;
};

type Props = {
    data: Insurance[];
};

export default function Insurances({ data }: Props) {
    return (
        <div className="insurances-box">
            <h2>Insurances</h2>
            <table className="components-table" border={1} cellPadding={8}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Insurance Provider</th>
                </tr>
                </thead>
                <tbody>
                {data.map(ins => (
                    <tr key={ins.insuranceId}>
                        <td>{ins.insuranceId}</td>
                        <td>{ins.insuranceProvider}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
