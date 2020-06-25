pipeline {
    agent any
    environment {
        CI = 'true'
    }
    stages {
        stage('Build java') {
            steps {
                sh 'docker-compose --version'
                sh 'pwd'
                sh 'ls -la'
                sh 'mvn --version'
                sh 'java --version'
//                sh 'docker-compose -f docker-compose.yml up -d --remove-orphans --build'
                step([$class: 'DockerComposeBuilder', dockerComposeFile: 'docker-compose.yml', option: [$class: 'ExecuteCommandInsideContainer', command: '', index: 1, privilegedMode: false, service: 'app', workDir: ''], useCustomDockerComposeFile: true])
            }
        }
    }
}
