# Multi-stage Dockerfile for Infrastructure DevTools Service

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven files
COPY Backend/Java/infrastructure-devtools-service/pom.xml .
COPY Backend/Java/infrastructure-devtools-service/src ./src

# Build the application
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install necessary packages
RUN apk add --no-cache \
    curl \
    bash \
    shadow-suexec

# Create non-root user
RUN addgroup -S devtools && adduser -S devtools -G devtools

# Copy the JAR file from builder
COPY --from=builder /app/target/infrastructure-devtools-service-*.jar app.jar

# Change ownership
RUN chown -R devtools:devtools /app

# Switch to non-root user
USER devtools

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
    CMD curl -f http://localhost:8080/api/devtools/health || exit 1

# Run the application
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
