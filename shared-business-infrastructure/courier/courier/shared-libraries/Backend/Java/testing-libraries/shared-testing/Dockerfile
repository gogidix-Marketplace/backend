# Enterprise-Grade Java Service Dockerfile Template
FROM eclipse-temurin:17-jdk-alpine AS build

# Set working directory
WORKDIR /workspace/app

# Copy Maven wrapper and dependency files first (for layer caching)
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this layer will be cached)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production stage
FROM eclipse-temurin:17-jre-alpine

# Create non-root user for security
RUN addgroup -g 1001 -S appuser && adduser -u 1001 -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy application layers from build stage
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build --chown=appuser:appuser ${DEPENDENCY}/BOOT-INF/lib ./lib
COPY --from=build --chown=appuser:appuser ${DEPENDENCY}/META-INF ./META-INF
COPY --from=build --chown=appuser:appuser ${DEPENDENCY}/BOOT-INF/classes .

# Switch to non-root user
USER appuser

# Default port (override in individual services)
EXPOSE 8700

# Health check (override URL in individual services)
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=40s \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8700/actuator/health || exit 1

# JVM optimization for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Application entry point (override main class in individual services)
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp .:lib/* com.gogidix.shared.shared-testing.Application"]