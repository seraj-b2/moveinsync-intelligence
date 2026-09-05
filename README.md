# MoveInSync Intelligence

Mobility Intelligence Platform with a Spring Boot Java backend and an Angular frontend.

---

## Quick Start (Run Locally)

You can launch both services together with:
```cmd
start-dev.bat
```

Or run them individually in separate terminals:

### 1. Backend (Spring Boot 4.x / Java 21)
```powershell
cd backend
.\gradlew.bat bootRun
```
- **Service URL**: [http://localhost:8080](http://localhost:8080)
- **Health Check**: [http://localhost:8080/api/health](http://localhost:8080/api/health)
- **Dashboard API**: [http://localhost:8080/api/dashboard/summary](http://localhost:8080/api/dashboard/summary)
- **Vendor Scorecards API**: [http://localhost:8080/api/vendors/scorecards](http://localhost:8080/api/vendors/scorecards)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:moveinsync`
  - User: `sa`
  - Password: *(blank)*

### 2. Frontend (Angular 18)
```powershell
cd frontend
npm start
```
- **Web App**: [http://localhost:4200](http://localhost:4200)

---

## Environment & Prerequisites Installed
- **Java**: Microsoft Build of OpenJDK 21 LTS (`JAVA_HOME` configured)
- **Node.js**: v24 LTS (`npm` v11)
- **Database**: Default is embedded in-memory H2 DB with auto-populated sample trip data (`DataInitializer.java`).
- **PostgreSQL (Optional)**: If you prefer PostgreSQL, a `docker-compose.yml` is provided in `infra/`.
