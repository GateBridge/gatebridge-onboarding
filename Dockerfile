# Stage 1: Build Executable Fat Jar
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Lightweight Production Runtime Container
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/gatebridge-onboarding-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 4000 9090 4011

# Dynamic RAM tuning for container environments
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar", "--headless"]
