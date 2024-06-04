FROM maven:3.6.3-openjdk-17 as builder
WORKDIR /app
COPY . /app
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package
# RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml -X test
# RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml liquibase:update

FROM openjdk:17-jdk-slim
ARG JAR_FILE=/app/target/*.jar
WORKDIR /app
COPY --from=builder $JAR_FILE /app/app.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

