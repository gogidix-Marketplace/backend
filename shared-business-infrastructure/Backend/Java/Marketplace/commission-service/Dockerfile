FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8131
ENTRYPOINT ["java", "-jar", "app.jar"]