FROM maven:3.9.12-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Shared library doesn't run as a service, but we keep the Dockerfile for consistency
# This Dockerfile can be used for testing the shared library

COPY --from=build /app/target/dashboard-shared-service-1.0.0.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

CMD ["echo", "Dashboard Shared Service - Library Component"]
