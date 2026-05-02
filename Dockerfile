# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the pom.xml first to leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the package
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create a non-root user for security (PCI-DSS compliance requirement)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the JAR from the build stage
COPY --from=build /app/target/tokenization-vault-core-*.jar app.jar

# Performance tuning for Java 21 Virtual Threads and Container awareness
# -XX:+UseZGC: Best for low-latency (< 10ms) applications
# -XX:MaxRAMPercentage: Ensures the JVM respects Docker memory limits
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8081

ENTRYPOINT ["sh", "-content", "java $JAVA_OPTS -jar app.jar"]