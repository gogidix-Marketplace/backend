# Multi-stage Dockerfile for Infrastructure Config Service

# Build stage
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install runtime dependencies
RUN apk add --no-cache dumb-init curl tzdata

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Set timezone
ENV TZ=UTC

WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/target/infrastructure-config-service-*.jar app.jar

# Change ownership
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8090/actuator/health || exit 1

# Expose port
EXPOSE 8090

# JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+PrintGCDetails -XX:+PrintGCTimeStamps"

# Entry point
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
