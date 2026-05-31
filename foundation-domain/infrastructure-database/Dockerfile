# Multi-stage build for Infrastructure Database Service
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="Gogidix Platform Team"
LABEL description="Infrastructure Database Service - Connection pooling, migrations, multi-tenant routing"

# Install required packages
RUN apk add --no-cache \
    curl \
    tzdata

# Create non-root user
RUN addgroup -S gogidix && adduser -S gogidix -G gogidix

# Set working directory
WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/infrastructure-database-service-*.jar app.jar

# Change ownership
RUN chown -R gogidix:gogidix /app

# Switch to non-root user
USER gogidix

# Expose port
EXPOSE 8091

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8091/actuator/health || exit 1

# JVM options
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Application profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
