FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/courier-api-gateway-1.0.0.jar app.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]