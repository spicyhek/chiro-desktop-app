import { Table, Column, Model, DataType, HasMany } from 'sequelize-typescript';
import { Appointment } from '.';

@Table
export class Doctor extends Model {
  @Column({
    type: DataType.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  })
  DoctorID!: number;

  @Column({
    type: DataType.STRING,
    allowNull: false,
  })
  Name!: string;

  @Column(DataType.STRING)
  Email?: string;

  @Column(DataType.STRING)
  PhoneNumber?: string;

  @Column({
    type: DataType.STRING,
    allowNull: false,
  })
  LicenseNumber!: string;

  @Column({
    type: DataType.DATE,
    defaultValue: DataType.NOW,
  })
  CreatedAt!: Date;

  @Column({
    type: DataType.DATE,
    defaultValue: DataType.NOW,
  })
  UpdatedAt!: Date;

  @HasMany(() => Appointment)
  appointments!: Appointment[];
} 