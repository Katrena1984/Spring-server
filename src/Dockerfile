FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

RUN chmod +x mvnw

ENTRYPOINT ["java", "-jar", "app.jar"]
