FROM eclipse-temurin:21-jre
WORKDIR /app
COPY inventory/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]