FROM eclipse-temurin:21-jre
WORKDIR /app
COPY order/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]