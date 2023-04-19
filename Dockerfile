FROM eclipse-temurin:11-alpine
WORKDIR /app
COPY target/*.jar recipe-book-api.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "recipe-book-api.jar"]