FROM maven:3.6.1-jdk-8
WORKDIR "/dwh"
COPY .git ./.git

WORKDIR "/dwh/build"
COPY migrator/pom.xml .
RUN mvn -ntp dependency:go-offline

COPY migrator/src ./src
RUN mvn -ntp package -DskipTests