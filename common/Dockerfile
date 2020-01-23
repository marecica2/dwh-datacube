FROM openjdk:8-jre-slim
COPY pom.xml .
ENV profile=default
COPY target/common.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${profile}", "-jar", "/app.jar"]