FROM openjdk:21-jdk-slim

ARG JAR_FILE=target/PettiWay-0.0.1-SNAPSHOT.jar
COPY ./target/PettiWay-0.0.1-SNAPSHOT.jar app_pettiway.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app_pettiway.jar"]
