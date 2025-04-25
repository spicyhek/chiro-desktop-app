import { Table, Column, Model, DataType, HasMany } from 'sequelize-typescript';
import { Patient } from '.';

@Table
export class Insurance extends Model {
  @Column({
    type: DataType.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  })
  InsuranceID!: number;

  @Column({
    type: DataType.STRING,
    allowNull: false,
  })
  InsuranceProvider!: string;

  @HasMany(() => Patient)
  patients!: Patient[];
} 