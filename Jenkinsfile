pipeline {
    agent any
    tools {
        maven 'm3'
        jdk 'jdk11'
    }
    environment {
        CI = 'true'
    }
    stages {
        stage('Build') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
                sh 'mvn -ntp clean install -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'docker-compose --version'
                sh 'pwd'
                sh 'ls -la'
                sh ' env $(cat .env.local)  docker-compose -f docker-compose.yml up -d --remove-orphans --build'
            }
        }
    }
}
