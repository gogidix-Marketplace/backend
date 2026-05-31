FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="platform-team@gogidix.com"
LABEL service="tenant-management-service"

WORKDIR /app

# Add the service jar
COPY target/tenant-management-service-*.jar app.jar

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider \
  http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
  "-jar", \
  "app.jar"]
