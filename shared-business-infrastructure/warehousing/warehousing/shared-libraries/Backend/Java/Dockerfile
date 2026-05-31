# Gogidix Shared Libraries - Production Dockerfile
# Multi-stage build for optimized image size

# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Copy pom files first for better layer caching
COPY pom.xml ./
COPY shared-libraries-bom/pom.xml shared-libraries-bom/

# Copy module pom files
COPY core-libraries/shared-exceptions/pom.xml core-libraries/shared-exceptions/
COPY core-libraries/shared-model/pom.xml core-libraries/shared-model/
COPY core-libraries/shared-validation/pom.xml core-libraries/shared-validation/
COPY core-libraries/shared-service-discovery/pom.xml core-libraries/shared-service-discovery/
COPY security-libraries/shared-audit/pom.xml security-libraries/shared-audit/
COPY security-libraries/shared-security/pom.xml security-libraries/shared-security/
COPY communication-libraries/shared-messaging/pom.xml communication-libraries/shared-messaging/
COPY testing-libraries/shared-testing/pom.xml testing-libraries/shared-testing/
COPY utility-libraries/shared-utilities/pom.xml utility-libraries/shared-utilities/
COPY shared-multitenancy/pom.xml shared-multitenancy/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B || true

# Copy all source code
COPY shared-libraries-bom/ shared-libraries-bom/
COPY core-libraries/ core-libraries/
COPY security-libraries/ security-libraries/
COPY communication-libraries/ communication-libraries/
COPY testing-libraries/ testing-libraries/
COPY utility-libraries/ utility-libraries/
COPY shared-multitenancy/ shared-multitenancy/

# Build all libraries
RUN mvn clean install -DskipTests -B

# Collect all built JARs into a single directory for easy copying
RUN mkdir -p /app/dist && \
    find /app/core-libraries -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    find /app/security-libraries -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    find /app/communication-libraries -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    find /app/testing-libraries -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    find /app/utility-libraries -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    find /app/shared-multitenancy -name "*.jar" -not -name "*-tests.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" -exec cp {} /app/dist/ \; && \
    ls -la /app/dist/

# Runtime stage - for libraries, we package JARs
FROM eclipse-temurin:17-jre-alpine

# Install utility tools
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -g 1000 gogidix && \
    adduser -D -u 1000 -G gogidix gogidix

WORKDIR /app

# Create lib directory
RUN mkdir -p /app/lib

# Copy all JAR files from the pre-populated dist directory
COPY --from=builder /app/dist/*.jar /app/lib/

# Change ownership
RUN chown -R gogidix:gogidix /app

USER gogidix

# Health check endpoint for CI/CD
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s \
    CMD echo "Shared libraries container healthy" || exit 1

# This is a library repository - JARs are dependencies for other projects
CMD ["echo", "Shared libraries built successfully. JARs available in /app/lib/"]
