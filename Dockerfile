# Multi-stage production Dockerfile for Academics Pro Platform
FROM eclipse-temurin:17-jre-jammy AS runtime

# Security: Add non-root system user and group
RUN groupadd -r academicspro && useradd -r -g academicspro academicspro

WORKDIR /app

# Copy the pre-built Spring Boot executable JAR
COPY target/academics-pro-platform-0.0.1-SNAPSHOT.jar app.jar

RUN chown -R academicspro:academicspro /app

USER academicspro:academicspro

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE="prod"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
