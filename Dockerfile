FROM maven:3.6-jdk-11 AS build
COPY . /app
RUN --mount=type=secret,id=recipe-book.properties,dst=/tmp/recipe-book.properties cp /tmp/recipe-book.properties /app/src/main/resources/recipe-book.properties
RUN mvn -f /app clean package

FROM eclipse-temurin:11-alpine
WORKDIR /api
COPY --from=build /app/target/*.jar /api/recipe-book-api.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "recipe-book-api.jar"]