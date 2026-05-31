@echo off
echo Starting Progress Step Service...
cd Backend\Java\progress-step-service
mvn clean install
mvn spring-boot:run
