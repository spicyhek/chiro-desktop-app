import sequelize from '../config/database';

async function testDatabaseConnection() {
  try {
    await sequelize.authenticate();
    console.log('Database connection successful');
  } catch (error) {
    console.error('Unable to connect to the database:', error);
    process.exit(1);
  } finally {
    await sequelize.close();
  }
}

testDatabaseConnection(); 