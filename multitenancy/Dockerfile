# Step : Test and package
FROM maven:3.5.4-jdk-8-alpine as target
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package -DskipTests

# Step : Package image
FROM openjdk:8-jre-alpine
EXPOSE 40001
ARG JAR_FILE=web-0.0.1-SNAPSHOT.jar

RUN pwd && echo 'content' && ls -la

COPY --from=target /build/target/${JAR_FILE} /app/web.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","web.jar"]
