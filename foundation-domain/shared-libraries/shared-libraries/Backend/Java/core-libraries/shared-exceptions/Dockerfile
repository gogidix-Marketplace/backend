# Multi-stage Dockerfile for Gogidix Shared Exceptions Service
# Optimized for production with security hardening and performance optimization
# Author: Gogidix Development Team
# Version: 1.0.0
# Date: 2025-08-13

# =================================================================
# Stage 1: Build Environment with Maven
# =================================================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set metadata labels
LABEL stage="builder"
LABEL service="shared-exceptions"

# Set working directory
WORKDIR /app

# Create non-root user for build process
RUN groupadd -r builduser && useradd -r -g builduser -d /app -s /bin/bash builduser

# Copy Maven configuration files first (for layer caching)
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (cached layer if pom.xml hasn't changed)
RUN ./mvnw dependency:resolve dependency:resolve-sources -B

# Copy source code
COPY src/ src/

# Change ownership to build user
RUN chown -R builduser:builduser /app

# Switch to non-root user for build
USER builduser

# Build the application with optimization
RUN ./mvnw clean package -DskipTests -B -T 1C \
    && ls -la target/ \
    && test -f target/shared-exceptions-1.0.0.jar

# =================================================================
# Stage 2: Runtime Environment with Security Hardening
# =================================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Set metadata labels
LABEL maintainer="Gogidix Development Team <dev@gogidix.com>"
LABEL service="shared-exceptions"
LABEL version="1.0.0"
LABEL description="Gogidix Shared Exceptions Service - Enterprise exception handling and monitoring"
LABEL build-date="2025-08-13"
LABEL port="8702"

# Install required packages and security updates
RUN apk add --no-cache \
    dumb-init \
    curl \
    wget \
    ca-certificates \
    fontconfig \
    ttf-dejavu \
    tzdata \
    && apk upgrade --no-cache \
    && rm -rf /var/cache/apk/*

# Set timezone
ENV TZ=UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create application directories with proper permissions
RUN mkdir -p /app/logs /app/config /app/tmp /app/heapdumps \
    && chmod 755 /app/logs /app/config /app/tmp /app/heapdumps

# Create non-root user for application security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup -h /app -s /bin/sh

# Set working directory
WORKDIR /app

# Copy the JAR file from builder stage
COPY --from=builder --chown=appuser:appgroup /app/target/shared-exceptions-1.0.0.jar app.jar

# Set proper ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user for security
USER appuser:appgroup

# Set optimized JVM environment variables
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=70.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:G1HeapRegionSize=16m \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -XX:+UseCompressedOops \
    -XX:+UseCompressedClassPointers \
    -XX:+UnlockExperimentalVMOptions \
    -XX:+UseCGroupMemoryLimitForHeap \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heapdumps/shared-exceptions-heapdump.hprof \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom \
    -Djava.awt.headless=true \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=UTC \
    -Dspring.jmx.enabled=true"

# Set Spring Boot environment variables
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8702
ENV MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics,prometheus
ENV MANAGEMENT_SERVER_PORT=8702
ENV LOGGING_FILE_NAME=/app/logs/shared-exceptions.log
ENV LOGGING_LEVEL_ROOT=INFO

# Expose application port
EXPOSE 8702

# Health check with proper endpoint
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8702/api/v1/actuator/health || exit 1

# Set volume mounts for persistence
VOLUME ["/app/logs", "/app/heapdumps"]

# Use dumb-init for proper signal handling and zombie reaping
ENTRYPOINT ["dumb-init", "--"]

# Start the application with optimized settings
CMD ["java", "-jar", "/app/app.jar"]