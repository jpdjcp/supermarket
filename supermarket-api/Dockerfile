# Stage 1: build the jar inside Maven
FROM maven:3.9.2-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar without running tests
RUN mvn clean package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/supermarket-api-0.0.1.jar ./supermarket-api.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "supermarket-api.jar"]