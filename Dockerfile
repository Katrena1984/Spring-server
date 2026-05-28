FROM eclipse-temurin:17-jdk-alpine AS build

RUN apk add --no-cache nodejs npm

WORKDIR /app

COPY .mvn ./.mvn
COPY mvnw ./mvnw
RUN chmod +x ./mvnw

COPY pom.xml ./
RUN ./mvnw dependency:go-offline -B

COPY src ./src
COPY client ./client

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]