FROM maven:3.9.12-eclipse-temurin-17 AS builder
WORKDIR /app
COPY settings.xml /root/.m2/settings.xml
COPY parent-pom.xml /tmp/parent-pom.xml
RUN --mount=type=cache,target=/root/.m2/repository mvn install:install-file -Dfile=/tmp/parent-pom.xml -DgroupId=com.gogidix.cargo.shared.infrastructure -DartifactId=shared-infrastructure-hexgonal -Dversion=1.0.0 -Dpackaging=pom -B
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2/repository mvn dependency:go-offline -B || true
COPY src ./src
RUN --mount=type=cache,target=/root/.m2/repository mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN groupadd -r cache && useradd -r -g cache cache
COPY --from=builder /app/target/caching-service-*.jar app/caching-service.jar
RUN chown -R cache:cache /app
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1
ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
USER cache
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/caching-service.jar"]
