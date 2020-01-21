FROM maven:3.6.1-jdk-8 as target
WORKDIR "/dwh"
COPY .git ./.git
COPY pom.xml .

WORKDIR "/dwh/build"

COPY common/pom.xml .
RUN mvn -ntp dependency:go-offline

COPY common/src ./src
RUN mvn -ntp package -DskipTests
CMD ["mvn", "-ntp ", "install"]