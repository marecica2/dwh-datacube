node {
    withEnv([
            'REDIS_HOST=localhost',
            'REDIS_PORT=6379',
            'PG_TEST_HOST=localhost',
            'PG_TEST_PORT=30002',
            'PG_TEST_DATABASE=dwh-test',
            'PG_TEST_USER=local-test',
            'PG_TEST_PASSWORD=local-test',
    ]
    ) {
        checkout scm
        stage("build") {
            sh 'java -version'
            sh 'mvn -version'
            sh 'mvn -ntp clean install -DskipTests'
        }
        stage("test") {
            docker.image('postgres:10.3').run(
                    '-e "POSTGRES_USER=$PG_TEST_USER" ' +
                            '-e "POSTGRES_PASSWORD=$PG_TEST_PASSWORD" ' +
                            '-e "POSTGRES_DB=$PG_TEST_DATABASE" ' +
                            '-p "$PG_TEST_PORT:5432" ' +
                            '-v $PWD/.db/init.sql:/docker-entrypoint-initdb.d/init.sql ' +
                            '--name pg_ci ',
                            'postgres -c log_statement=all '
            )

            sh './wait-for.sh localhost:$PG_TEST_PORT -- echo postgres is ready'

            docker.image('redis:4.0.5-alpine').withRun(
                    '-p "$REDIS_PORT:6379" ' +
                            '--name redis_ci')
                    { redis ->
                        sh './wait-for.sh localhost:$REDIS_PORT -- echo redis is ready'
                        sh 'env $(cat .env.local) mvn exec:java -pl app-migrator  -Dspring.profiles.active=cli,integration-test'
                        sh 'mvn -ntp clean install'
                    }
        }
    }
}
