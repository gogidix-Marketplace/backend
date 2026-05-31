# Dockerfile for Shared Messaging Library
# Multi-stage build for optimized production image
# Spring Boot 3.1.5 with Java 17

# =====================================
# STAGE 1: Build Stage
# =====================================
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy Maven files for dependency caching
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src/ ./src/

# Build application
RUN mvn clean package -DskipTests -B

# =====================================
# STAGE 2: Runtime Stage
# =====================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Install required packages
RUN apk add --no-cache \
    curl \
    bash \
    tzdata \
    netcat-openbsd \
    dumb-init

# Create application user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/shared-messaging-1.0.0.jar app.jar

# Copy entrypoint script
COPY docker/entrypoint.sh .
RUN chmod +x entrypoint.sh

# Change ownership to app user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose messaging service port
EXPOSE 8501

# Health check for shared-messaging service
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8501/api/messaging/actuator/health || exit 1

# Environment variables
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE="docker" \
    JVM_MEMORY="-Xmx512m -Xms256m" \
    SERVER_PORT="8501"

# Use dumb-init for proper signal handling
ENTRYPOINT ["dumb-init", "--", "./entrypoint.sh"]