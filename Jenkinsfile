node {
    checkout scm
    stage("build") {
        docker.image('postgres:10.3').withRun(
                '-e "PG_USER=postgres" ' +
                        '-e "PG_USER=postgres" ' +
                        '-e "PG_PASSWORD=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-p "5432:5432" ' +
                        '--name pg_ci') { c ->
            sh 'java -version'
            sh 'mvn -version'
            sh './wait-for.sh localhost:5432 -- echo postgres is ready'
        }
    }
}
