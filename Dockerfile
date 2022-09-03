FROM openjdk:17-jdk-slim-buster
ADD target/url-shorterner-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]