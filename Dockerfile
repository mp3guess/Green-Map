FROM ubuntu:latest
LABEL authors="ivantamrazov"

FROM eclipse-temurin:17.0.8_7-jre
EXPOSE 8080
WORKDIR /opt/app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Set environment variables for database configuration
ENV DATABASE_URL=jdbc:postgresql://db:5432/green_map
ENV DATABASE_USER=postgres
ENV DATABASE_PASSWORD=postgres
ENV REDIS_HOST=redis
ENV REDIS_PORT=6379


ENTRYPOINT ["java", "-jar", "app.jar"]
