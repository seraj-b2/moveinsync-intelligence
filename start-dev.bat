@echo off
echo ========================================================
echo Starting MoveInSync Intelligence (Backend & Frontend)
echo ========================================================

set "JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot"
set "PATH=%JAVA_HOME%\bin;C:\Program Files\nodejs;%PATH%"

echo [1/2] Starting Spring Boot Backend on http://localhost:8080 ...
start "MoveInSync Backend (Spring Boot)" cmd /k "cd /d %~dp0backend && .\gradlew.bat bootRun"

echo [2/2] Starting Angular Frontend on http://localhost:4200 ...
start "MoveInSync Frontend (Angular)" cmd /k "cd /d %~dp0frontend && npm start"

echo.
echo Both servers are launching in separate windows!
echo Backend API:  http://localhost:8080/api/health
echo Frontend Web: http://localhost:4200
echo H2 DB Console: http://localhost:8080/h2-console
echo ========================================================
