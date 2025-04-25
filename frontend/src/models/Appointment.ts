import { Table, Column, Model, DataType, ForeignKey, BelongsTo, HasOne } from 'sequelize-typescript';
import { Patient, Doctor, Record } from '.';

@Table
export class Appointment extends Model {
  @Column({
    type: DataType.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  })
  AppointmentID!: number;

  @ForeignKey(() => Patient)
  @Column(DataType.INTEGER)
  PatientID!: number;

  @BelongsTo(() => Patient)
  patient!: Patient;

  @ForeignKey(() => Doctor)
  @Column(DataType.INTEGER)
  DoctorID!: number;

  @BelongsTo(() => Doctor)
  doctor!: Doctor;

  @Column({
    type: DataType.DATE,
    allowNull: false,
  })
  ScheduledDateTime!: Date;

  @Column({
    type: DataType.ENUM('Scheduled', 'Completed', 'Cancelled'),
    allowNull: false,
  })
  Status!: 'Scheduled' | 'Completed' | 'Cancelled';

  @Column(DataType.TEXT)
  AppointmentNotes?: string;

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

  @HasOne(() => Record)
  record!: Record;
} 