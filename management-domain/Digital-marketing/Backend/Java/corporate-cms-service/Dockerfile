# Multistage build for Corporate CMS Service
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="dev@gogidix.com"
LABEL description="Corporate CMS Service - Headless CMS backend for managing corporate website content"

# Install dumb-init for proper signal handling
RUN apk add --no-cache dumb-init curl

# Create app user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set up directories
WORKDIR /app
RUN mkdir -p /app/logs /app/uploads && \
    chown -R appuser:appgroup /app

# Copy JAR from build stage
COPY --from=build /app/target/corporate-cms-service-*.jar app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/cms/v1/actuator/health || exit 1

# Run the application
ENTRYPOINT ["dumb-init", "--"]
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dspring.profiles.active=prod", \
     "-jar", \
     "app.jar"]
