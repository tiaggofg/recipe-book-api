FROM maven:3.6-jdk-11 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package

FROM eclipse-temurin:11-alpine as prod
WORKDIR /app
COPY --from=build /app/target/*.jar recipe-book-api.jar
EXPOSE 5555
HEALTHCHECK --interval=5m --timeout=3s \
  CMD curl -f http://localhost:5555/status || exit 1
ENTRYPOINT ["java", "-jar", "recipe-book-api.jar"]