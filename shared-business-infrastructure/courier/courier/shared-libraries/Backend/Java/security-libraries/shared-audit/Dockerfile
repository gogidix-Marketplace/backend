# Multi-stage build for shared-audit service
# Stage 1: Build stage
FROM eclipse-temurin:17-jdk-slim AS builder

LABEL maintainer="Gogidix Technologies <dev@gogidix.com>"
LABEL description="Shared Audit Library - Comprehensive audit logging and tracking library"
LABEL version="1.0.0"

# Set working directory
WORKDIR /app

# Copy Maven wrapper and POM for dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (layer caching optimization)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application with security optimizations
RUN ./mvnw clean package -DskipTests -B \
    -Dspring-boot.build-image.env.BP_JVM_VERSION=17 \
    -Dspring-boot.build-image.env.BPE_DELIM_JAVA_TOOL_OPTIONS=" " \
    -Dspring-boot.build-image.env.BPE_APPEND_JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:+UseG1GC"

# Stage 2: Runtime stage with security hardening
FROM eclipse-temurin:17-jre-slim

LABEL maintainer="Gogidix Technologies <dev@gogidix.com>"
LABEL description="Shared Audit Library - Production Runtime"
LABEL version="1.0.0"
LABEL org.opencontainers.image.title="shared-audit"
LABEL org.opencontainers.image.description="Enterprise audit logging and compliance tracking service"
LABEL org.opencontainers.image.vendor="Gogidix Technologies"
LABEL org.opencontainers.image.licenses="Proprietary"

# Create non-root user for security
RUN groupadd -r -g 1001 audit && \
    useradd -r -g audit -u 1001 -m -d /home/audit -s /bin/bash audit

# Set working directory
WORKDIR /home/audit

# Install security updates and required packages
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends \
        curl \
        ca-certificates \
        tzdata && \
    rm -rf /var/lib/apt/lists/* && \
    apt-get clean

# Copy JAR from builder stage
COPY --from=builder /app/target/shared-audit-*.jar app.jar

# Create logs and config directories with proper permissions
RUN mkdir -p logs config && \
    chown -R audit:audit /home/audit && \
    chmod 755 /home/audit && \
    chmod 644 app.jar

# Switch to non-root user for security
USER audit

# Environment variables for production
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError" \
    SPRING_PROFILES_ACTIVE=production \
    SERVER_PORT=8701 \
    MANAGEMENT_SERVER_PORT=8701 \
    TZ=UTC \
    LOG_LEVEL_ROOT=INFO \
    LOG_LEVEL_AUDIT=INFO

# Expose application port
EXPOSE 8701

# Health check for container orchestration
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8701/actuator/health || exit 1

# Define volumes for persistent data
VOLUME ["/home/audit/logs", "/home/audit/config"]

# Security: Run as non-root, set read-only filesystem
USER 1001:1001

# Optimized entry point with security considerations
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.config.additional-location=optional:file:/home/audit/config/ \
    -Dlogging.file.path=/home/audit/logs \
    -jar app.jar"]