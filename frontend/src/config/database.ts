import { Sequelize } from 'sequelize-typescript';
import path from 'path';
import { app } from 'electron';

const userDataPath = app.getPath('userData');
const dbPath = path.join(userDataPath, 'chiropractic.sqlite');

const sequelize = new Sequelize({
  dialect: 'sqlite',
  storage: dbPath,
  models: [__dirname + '/../models'],
  logging: false,
});

export default sequelize; 