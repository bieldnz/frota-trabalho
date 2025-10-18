# syntax=docker/dockerfile:1.5

# --- Build stage ---
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy POM and resolve dependencies (layer caching)
COPY pom.xml ./
RUN mvn -B -q -DskipTests dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn -B -DskipTests clean package

# --- Runtime stage ---
FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /

# Create non-root user
RUN useradd -m -u 1000 appuser

# Copy fat jar from builder
ARG JAR_FILE=/app/target/*.jar
COPY --from=builder ${JAR_FILE} /app.jar
RUN chown appuser:appuser /app.jar

# Expose app port
EXPOSE 8083

# JVM options can be injected via JAVA_OPTS env var
ENV JAVA_OPTS=""

# Use non-root user
USER appuser

# Run the application
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app.jar"]
