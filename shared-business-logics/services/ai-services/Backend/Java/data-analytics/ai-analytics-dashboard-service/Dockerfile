# Multi-stage build for AI Analytics Dashboard Service
# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build

LABEL maintainer="Gogidix Platform Team <platform@gogidix.com>"
LABEL description="AI Analytics Dashboard Service - AI Service"

WORKDIR /app

# Copy pom.xml first for better Docker layer caching
COPY pom.xml .

# Download dependencies (cached layer if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Add non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Install curl for health check
RUN apk add --no-cache curl ca-certificates

WORKDIR /app

# Copy the built JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Create log directory with proper permissions
RUN mkdir -p /app/logs && \
    chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose application port
EXPOSE 8080

# Add labels for metadata
LABEL org.opencontainers.image.title="AI Analytics Dashboard Service"
LABEL org.opencontainers.image.description="AI Service"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.vendor="Gogidix"

# Health check using Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM tuning for containerized environment
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-XX:MaxGCPauseMillis=200", \
  "-XX:+HeapDumpOnOutOfMemoryError", \
  "-XX:HeapDumpPath=/app/logs/", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=prod", \
  "-jar", \
  "app.jar"]
