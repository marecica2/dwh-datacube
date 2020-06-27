node {
    checkout scm
    stage("build") {
        docker.image('postgres:10.3').withRun(
                '-e "PG_USER=postgres" ' +
                        '-e "PG_USER=postgres" ' +
                        '-e "PG_PASSWORD=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-n pg_test') { c ->
            sh 'java -version'
            sh 'mvn -version'
            sh './wait-for localhost:5432 -- echo postgres is ready'
        }
    }
}
