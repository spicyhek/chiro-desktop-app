import { Table, Column, Model, DataType, ForeignKey, BelongsTo, HasMany } from 'sequelize-typescript';
import { Insurance, Appointment } from '.';

@Table
export class Patient extends Model {
  @Column({
    type: DataType.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  })
  PatientID!: number;

  @Column({
    type: DataType.STRING,
    allowNull: false,
  })
  Name!: string;

  @Column({
    type: DataType.DATE,
    allowNull: false,
  })
  DateOfBirth!: Date;

  @Column(DataType.STRING)
  Email?: string;

  @Column(DataType.STRING)
  PhoneNumber?: string;

  @ForeignKey(() => Insurance)
  @Column(DataType.INTEGER)
  InsuranceID?: number;

  @BelongsTo(() => Insurance)
  insurance?: Insurance;

  @Column(DataType.STRING)
  EmergencyContactName?: string;

  @Column(DataType.STRING)
  EmergencyContactPhone?: string;

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