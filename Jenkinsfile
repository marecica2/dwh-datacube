pipeline {
    agent any
    environment {
        CI = 'true'
    }
    tools {
        maven 'M3'
    }
    stages {
        stage('Build java') {
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
