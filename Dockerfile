FROM eclipse-temurin:11-alpine
COPY target/recipe-book-api-0.0.3-SNAPSHOT.jar /recipe-book-api-0.0.3-SNAPSHOT.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "recipe-book-api-0.0.3-SNAPSHOT.jar"]
