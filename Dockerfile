# Stage 1: Build Jar
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Production Lightweight Image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/gatebridge-onboarding-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 4000 9090

ENTRYPOINT ["java", "-jar", "app.jar", "--headless"]
