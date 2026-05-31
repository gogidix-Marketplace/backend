# Multi-stage build for centralized-api-gateway
FROM openjdk:17-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM openjdk:17-jre-slim

# Create non-root user for security
RUN groupadd -r gateway && useradd -r -g gateway gateway

# Set working directory
WORKDIR /app

# Install required packages
RUN apt-get update && apt-get install -y \
    curl \
    netcat-traditional \
    && rm -rf /var/lib/apt/lists/*

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Create logs directory
RUN mkdir -p /var/log/centralized-api-gateway && \
    chown -R gateway:gateway /var/log/centralized-api-gateway && \
    chown -R gateway:gateway /app

# Switch to non-root user
USER gateway

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Expose port
EXPOSE 8080

# Environment variables with defaults
ENV SPRING_PROFILES_ACTIVE=prod
ENV JVM_OPTS="-Xmx1024m -Xms512m -XX:+UseG1GC -XX:+UseContainerSupport"
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS $JAVA_OPTS -jar app.jar"]

# Labels for metadata
LABEL maintainer="gogidix-technology"
LABEL service="centralized-api-gateway"
LABEL version="1.0.0"
LABEL description="Centralized Dashboard API Gateway Service"