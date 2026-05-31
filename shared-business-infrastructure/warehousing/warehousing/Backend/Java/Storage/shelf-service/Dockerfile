FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8224
ENTRYPOINT ["java", "-jar", "app.jar"]