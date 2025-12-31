FROM openjdk:21.0.6
ARG JAR_FILE=target/supermarket-api-0.0.1.jar
COPY ${JAR_FILE} supermarket-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "supermarket-api.jar"]