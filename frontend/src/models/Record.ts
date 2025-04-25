import { Table, Column, Model, DataType, ForeignKey, BelongsTo } from 'sequelize-typescript';
import { Patient, Doctor, Appointment } from '.';

@Table
export class Record extends Model {
  @Column({
    type: DataType.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  })
  RecordID!: number;

  @ForeignKey(() => Appointment)
  @Column(DataType.INTEGER)
  AppointmentID!: number;

  @BelongsTo(() => Appointment)
  appointment!: Appointment;

  @Column({
    type: DataType.DATE,
    allowNull: false,
  })
  VisitDate!: Date;

  @Column(DataType.TEXT)
  Symptoms?: string;

  @Column(DataType.TEXT)
  Diagnosis?: string;

  @Column(DataType.DATE)
  NextVisitRecommendedDate?: Date;

  @Column(DataType.TEXT)
  RecordNotes?: string;

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
} 