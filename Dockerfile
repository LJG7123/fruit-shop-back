# Multi-stage build for Spring Boot (Gradle)
# Build stage
FROM gradle:8.4-jdk17 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
# Use the Gradle wrapper if present; skip tests for faster CI builds
RUN gradle bootJar --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=build/libs/*.jar
COPY --from=build /home/gradle/project/build/libs/*.jar /app/app.jar
ENV SPRING_PROFILES_ACTIVE=prod
# Expose application port
EXPOSE 8080
# Use a small JVM tuning option for faster startup
ENTRYPOINT ["sh", "-c", "exec java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
