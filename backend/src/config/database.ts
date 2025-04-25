import { Sequelize } from 'sequelize-typescript';
import path from 'path';

const dbPath = path.join(__dirname, '..', '..', 'chiropractic.sqlite');

const sequelize: Sequelize = new Sequelize({
  dialect: 'sqlite',
  storage: dbPath,
  logging: false,
});

export default sequelize; 