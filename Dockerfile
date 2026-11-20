# ---------- Stage 1: build the jar ----------

FROM maven:3.10.0-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

# ---------- Stage 2: run the jar ----------
FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/swish-0.0.1-SNAPSHOT.jar app.jar
RUN addgroup -S swish && adduser -S swish -G swish
USER swish

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
