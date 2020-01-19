FROM maven:3.6.0-jdk-8-slim
WORKDIR "/dwh"
COPY .git ./.git

WORKDIR "/dwh/build"

COPY migrator/pom.xml .
RUN mvn dependency:go-offline

COPY migrator/src ./src
RUN mvn package -DskipTests
CMD ["mvn", "exec:java"]