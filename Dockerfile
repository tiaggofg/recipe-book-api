FROM maven:3.6-jdk-11 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:11-alpine
WORKDIR /app
COPY /target/*.jar /app/recipe-book-api.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "recipe-book-api.jar"]