# AI Monitoring Service - Multi-stage Dockerfile
# Production-ready image with minimal runtime

# Stage 1: Builder
FROM maven:3.9.12-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom.xml and source
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml
COPY src ./src

# Build the application
# Skip tests and non-relevant plugins for container build
RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -DskipTests \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true \
    -Djacoco.skip=true \
    -Dspring-boot.run.fork=false

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init bash && \
    rm -rf /var/cache/apk/*

# Create app directory and non-root user
RUN mkdir -p /app && \
    addgroup -S spring && \
    adduser -S spring -G spring && \
    chown -R spring:spring /app

# Copy JAR from builder
COPY --from=builder /build/target/*.jar /app/app.jar

# Switch to non-root user
USER spring:spring

# Expose port
EXPOSE 8001

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8001/actuator/health || exit 1

# Use dumb-init to handle signals properly
ENTRYPOINT ["dumb-init", "--"]

# Run the application
CMD ["java", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+UseContainerSupport", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", "/app/app.jar"]
