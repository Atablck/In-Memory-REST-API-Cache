FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build

COPY pom.xml .
COPY src ./src


RUN mvn clean package



FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=builder /build/target/app.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]