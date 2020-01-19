FROM maven:3.6.0-jdk-8-slim
WORKDIR "/dwh"
COPY .git ./.git

WORKDIR "/dwh/build"
RUN cat /etc/hosts
COPY migrator/pom.xml .
RUN mvn dependency:go-offline

COPY migrator/src ./src
RUN mvn package -DskipTests -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn