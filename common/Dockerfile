FROM maven:3.6.1-jdk-8-slim as target
WORKDIR "/dwh"
COPY .git ./.git

WORKDIR "/dwh/build"

COPY common/pom.xml .
RUN mvn -ntp dependency:go-offline

COPY common/src ./src
CMD ["mvn", "-ntp ", "install"]