pipeline {
    agent any
    environment {
        CI = 'true'
    }
    tools {
        maven 'M3'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -ntp clean install -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'docker-compose --version'
                sh 'pwd'
                sh 'ls -la'
                sh 'mvn -version'
                sh 'java -version'
                sh ' env $(cat .env.local)  docker-compose -f docker-compose.yml up -d --remove-orphans --build'
            }
        }
    }
}
