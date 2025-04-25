import sequelize from '../config/database';
import { Patient, Doctor, Appointment, Record, Insurance } from '../models';

export class DatabaseService {
  static async initialize() {
    try {
      await sequelize.authenticate();
      console.log('Database connection established.');

      // Sync all models with the database
      await sequelize.sync({ force: true });
      console.log('Database synchronized');
    } catch (error) {
      console.error('Database connection failed:', error);
      throw error;
    }
  }

  // Patient operations
  static async createPatient(patientData: Partial<Patient>) {
    return await Patient.create(patientData);
  }

  static async getPatient(id: number) {
    return await Patient.findByPk(id, {
      include: [
        { model: Insurance },
        { model: Appointment, include: [Record] }
      ]
    });
  }

  static async getAllPatients() {
    return await Patient.findAll({
      include: [
        { model: Insurance },
        { model: Appointment, include: [Record] }
      ]
    });
  }

  static async updatePatient(id: number, patientData: Partial<Patient>) {
    const patient = await Patient.findByPk(id);
    if (!patient) throw new Error('Patient not found');
    return await patient.update(patientData);
  }

  static async deletePatient(id: number) {
    const patient = await Patient.findByPk(id);
    if (!patient) throw new Error('Patient not found');
    return await patient.destroy();
  }

  // Doctor operations
  static async createDoctor(doctorData: Partial<Doctor>) {
    return await Doctor.create(doctorData);
  }

  static async getDoctor(id: number) {
    return await Doctor.findByPk(id, {
      include: [Appointment]
    });
  }

  static async getAllDoctors() {
    return await Doctor.findAll({
      include: [Appointment]
    });
  }

  static async updateDoctor(id: number, doctorData: Partial<Doctor>) {
    const doctor = await Doctor.findByPk(id);
    if (!doctor) throw new Error('Doctor not found');
    return await doctor.update(doctorData);
  }

  static async deleteDoctor(id: number) {
    const doctor = await Doctor.findByPk(id);
    if (!doctor) throw new Error('Doctor not found');
    return await doctor.destroy();
  }

  // Appointment operations
  static async createAppointment(appointmentData: Partial<Appointment>) {
    return await Appointment.create(appointmentData);
  }

  static async getAppointment(id: number) {
    return await Appointment.findByPk(id, {
      include: [Patient, Doctor, Record]
    });
  }

  static async getAllAppointments() {
    return await Appointment.findAll({
      include: [Patient, Doctor, Record]
    });
  }

  static async updateAppointment(id: number, appointmentData: Partial<Appointment>) {
    const appointment = await Appointment.findByPk(id);
    if (!appointment) throw new Error('Appointment not found');
    return await appointment.update(appointmentData);
  }

  static async deleteAppointment(id: number) {
    const appointment = await Appointment.findByPk(id);
    if (!appointment) throw new Error('Appointment not found');
    return await appointment.destroy();
  }

  // Record operations
  static async createRecord(recordData: Partial<Record>) {
    return await Record.create(recordData);
  }

  static async getRecord(id: number) {
    return await Record.findByPk(id, {
      include: [Appointment]
    });
  }

  static async updateRecord(id: number, recordData: Partial<Record>) {
    const record = await Record.findByPk(id);
    if (!record) throw new Error('Record not found');
    return await record.update(recordData);
  }

  static async deleteRecord(id: number) {
    const record = await Record.findByPk(id);
    if (!record) throw new Error('Record not found');
    return await record.destroy();
  }

  // Insurance operations
  static async createInsurance(insuranceData: Partial<Insurance>) {
    return await Insurance.create(insuranceData);
  }

  static async getInsurance(id: number) {
    return await Insurance.findByPk(id, {
      include: [Patient]
    });
  }

  static async getAllInsurance() {
    return await Insurance.findAll({
      include: [Patient]
    });
  }

  static async updateInsurance(id: number, insuranceData: Partial<Insurance>) {
    const insurance = await Insurance.findByPk(id);
    if (!insurance) throw new Error('Insurance not found');
    return await insurance.update(insuranceData);
  }

  static async deleteInsurance(id: number) {
    const insurance = await Insurance.findByPk(id);
    if (!insurance) throw new Error('Insurance not found');
    return await insurance.destroy();
  }
} 