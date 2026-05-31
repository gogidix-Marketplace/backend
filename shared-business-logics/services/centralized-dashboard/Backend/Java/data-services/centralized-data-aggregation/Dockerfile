# Multi-stage build for centralized-data-aggregation service
# Use Maven image directly to bypass problematic wrapper
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy parent POM to root level
COPY pom.xml ./

# Copy the entire centralized-data-aggregation module
COPY centralized-data-aggregation ./centralized-data-aggregation

# Change to the module directory
WORKDIR /app/centralized-data-aggregation

# Make mvnw executable (in case it's not)
RUN chmod +x mvnw

# Update the relative path in child POM to point to the parent POM
RUN sed -i 's|<relativePath>../pom.xml</relativePath>|<relativePath>../pom.xml</relativePath>|' pom.xml

# Use Maven directly instead of wrapper to avoid argument parsing issues
# Download dependencies
RUN mvn dependency:go-offline -B

# Build the application  
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/centralized-data-aggregation/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
