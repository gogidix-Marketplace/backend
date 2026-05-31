# Multi-stage build for Auth Service
# Stage 1: Build
FROM maven:3.9.12-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml and download dependencies (better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="Gogidix Infrastructure Team"
LABEL app="auth-service"
LABEL version="1.0.0"
LABEL description="Authentication and Authorization Service - JWT/OAuth2 multi-tenant authentication"

# Create non-root user
RUN groupadd -r auth && useradd -r -g auth auth

# Set working directory
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /build/target/auth-service-*.jar app/auth-service.jar

# Change ownership
RUN chown -R auth:auth /app

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM options
ENV JAVA_OPTS="-Xms512m -Xmx512m \
                 -XX:+UseG1GC \
                 -XX:MaxGCPauseMillis=200 \
                 -XX:+HeapDumpOnOutOfMemoryError \
                 -XX:HeapDumpPath=/app/logs \
                 -Djava.security.egd=file:/dev/./urandom"

# Run as non-root user
USER auth

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/auth-service.jar"]
