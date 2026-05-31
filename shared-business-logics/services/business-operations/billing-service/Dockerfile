# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy

LABEL maintainer="Gogidix Infrastructure Team"
LABEL app="billing-service"
LABEL version="1.0.0"
LABEL description="Billing Service - Subscription and invoice management"

RUN groupadd -r billing && useradd -r -g billing billing

WORKDIR /app

COPY --from=builder /build/target/billing-service-*.jar app/billing-service.jar

RUN chown -R billing:billing /app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

USER billing

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/billing-service.jar"]
