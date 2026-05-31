@echo off
echo Starting Audit Trail Service...
cd Backend\Java\audit-trail-service
mvn clean install
mvn spring-boot:run
