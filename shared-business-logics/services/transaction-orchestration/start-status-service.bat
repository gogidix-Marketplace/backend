@echo off
echo Starting Status Broadcast Service...
cd Backend\Java\status-broadcast-service
mvn clean install
mvn spring-boot:run
