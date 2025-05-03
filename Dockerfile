FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY wait-for-it.sh .
COPY --from=builder /app/target/*.jar app.jar
RUN chmod +x wait-for-it.sh

EXPOSE 8080
ENTRYPOINT ["./wait-for-it.sh", "postgres:5432", "-t", "30", "--", "java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]
