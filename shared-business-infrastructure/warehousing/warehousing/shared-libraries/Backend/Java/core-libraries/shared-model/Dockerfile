# =============================================================================
# GOGIDIX SHARED MODEL LIBRARY SERVICE - MULTI-STAGE DOCKERFILE
# =============================================================================
# Service: shared-model-service
# Port: 8704 (Shared Model Library Service)
# Architecture: Hexagonal Architecture with Domain-Driven Design
# Base Image: Eclipse Temurin JDK 17 (Security-hardened)
# =============================================================================

# =============================================================================
# STAGE 1: BUILD STAGE
# =============================================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /workspace/app

# Install build tools and dependencies
RUN apk add --no-cache \
    curl \
    bash \
    findutils \
    && rm -rf /var/cache/apk/*

# Copy Maven wrapper and configuration files
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (for better Docker layer caching)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests -B \
    && mkdir -p target/dependency \
    && (cd target/dependency; jar -xf ../*.jar)

# =============================================================================
# STAGE 2: RUNTIME STAGE
# =============================================================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# Metadata
LABEL maintainer="Gogidix Development Team <dev@gogidix.com>"
LABEL description="Shared Model Library Service - Common domain models and DTOs"
LABEL version="1.0.0"
LABEL service="shared-model-service"
LABEL domain="shared-libraries"
LABEL port="8704"
LABEL architecture="hexagonal"

# Install runtime dependencies and create non-root user
RUN apk add --no-cache \
    curl \
    bash \
    tzdata \
    dumb-init \
    && addgroup -g 1001 -S appgroup \
    && adduser -u 1001 -S appuser -G appgroup \
    && rm -rf /var/cache/apk/*

# Set timezone
ENV TZ=UTC

# Create application directory
RUN mkdir -p /app/logs /app/config /app/data \
    && chown -R appuser:appgroup /app

# Set working directory
WORKDIR /app

# Copy application files from build stage
COPY --from=builder --chown=appuser:appgroup /workspace/app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=builder --chown=appuser:appgroup /workspace/app/target/dependency/META-INF /app/META-INF
COPY --from=builder --chown=appuser:appgroup /workspace/app/target/dependency/BOOT-INF/classes /app

# Copy additional configuration files
COPY --chown=appuser:appgroup docker/entrypoint.sh /app/entrypoint.sh
COPY --chown=appuser:appgroup docker/health-check.sh /app/health-check.sh

# Make scripts executable
RUN chmod +x /app/entrypoint.sh /app/health-check.sh

# Switch to non-root user
USER appuser:appgroup

# Set JVM options for container environment
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -XX:+UnlockExperimentalVMOptions \
               -XX:+UseCGroupMemoryLimitForHeap \
               -XX:+ExitOnOutOfMemoryError \
               -Djava.security.egd=file:/dev/./urandom \
               -Dspring.backgroundpreinitializer.ignore=true \
               -Dspring.config.location=classpath:/application.yml"

# Application configuration
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8704
ENV LOGGING_LEVEL_ROOT=INFO

# Expose port
EXPOSE 8704

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /app/health-check.sh

# Volume for logs and temporary data
VOLUME ["/app/logs"]

# Entry point
ENTRYPOINT ["dumb-init", "--", "/app/entrypoint.sh"]

# Default command
CMD ["java", "-cp", ".:lib/*", "com.gogidix.libraries.sharedmodel.SharedModelApplication"]