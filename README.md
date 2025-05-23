# Chiropractor Database

## Project Overview
This is a full-stack web application for managing a chiropractor’s data:
- **Frontend**: React + TypeScript UI to view, add, update, delete, and search Appointments, Patients, Doctors, Records, and Insurances.
- **Backend**: Spring Boot REST API with custom DAO/Service layers, using SQLite (or your JDBC-compatible database) for persistence.

---

## Getting Started

### Prerequisites
- **Java 17+**
- **Maven** (or Gradle) for building the backend
- **Node.js 16+** and **npm** (or Yarn) for the frontend
- **SQLite 3** (or switch to another JDBC-compatible DB if desired)


## Running Application
Through root, run the following commands then click the link that appears once the command runs:

```
cd frontend
npm install
npm run dev
```
To run, the backend must compiled and ran through the main class, ChiroApplication. This starts the Spring Boot server.

```
cd backend/src/main/java/com/chiro/util/ChiroApplication
```
