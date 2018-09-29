FROM maven:alpine

WORKDIR /app
COPY . /app

RUN pwd && ls -la

RUN mvn package -DskipTests