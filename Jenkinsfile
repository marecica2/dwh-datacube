node {
    checkout scm
    stage("build") {
        sh 'java -version'
        sh 'mvn -version'
    }
    stage("test") {
        docker.image('postgres:10.3').withRun(
                '-e "PG_USER=postgres" ' +
                        '-e "PG_USER=postgres" ' +
                        '-e "PG_PASSWORD=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-e "POSTGRES_DB=postgres" ' +
                        '-p "5432:5432" ' +
                        '--name pg_ci')
                { pg ->

                    sh './wait-for.sh localhost:5432 -- echo postgres is ready'

                    docker.image('redis:4.0.5-alpine').withRun(
                            '-p "6379:6379" ' +
                                    '--name redis_ci')
                            { redis ->
                                sh './wait-for.sh localhost:6379 -- echo redis is ready'
                            }
                }
    }
}
