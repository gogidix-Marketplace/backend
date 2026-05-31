# Build stage
FROM maven:3.9.12-eclipse-temurin-17 AS builder
WORKDIR /app
COPY settings.xml /root/.m2/settings.xml
COPY parent-pom.xml /tmp/parent-pom.xml
RUN --mount=type=cache,target=/root/.m2/repository mvn install:install-file -Dfile=/tmp/parent-pom.xml -DgroupId=com.gogidix.cargo.shared.infrastructure -DartifactId=shared-infrastructure-hexgonal -Dversion=1.0.0 -Dpackaging=pom -B
COPY pom.xml .
COPY src ./src
RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/shared-infrastructure-api-gateway-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
