@echo off
echo Starting Onboarding Tracker Service...
cd Backend\Java\onboarding-tracker-service
mvn clean install
mvn spring-boot:run
