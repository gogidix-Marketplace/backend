@echo off
echo Starting Transaction Monitoring Service...
cd Backend\Java\transaction-monitoring-service
mvn clean install
mvn spring-boot:run
