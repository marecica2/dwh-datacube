FROM maven:3.6.1-jdk-8 as target
WORKDIR "/dwh"
COPY .git ./.git
COPY pom.xml .

WORKDIR "/dwh/build"

RUN tree -d /root/.m2/org/bmsource/

COPY importer/pom.xml .
RUN mvn -ntp dependency:go-offline

COPY importer/src ./src
CMD ["mvn", "-ntp ", "install"]