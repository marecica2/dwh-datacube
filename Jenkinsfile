node {
    checkout scm
    stage("build") {
        docker.image('postgres:10.3').withRun(
                '-e "PG_USER=postgres" ' +
                        '-e "PG_USER=postgres" ' +
                        '-e "PG_PASSWORD=postgres" ' +
                        '-e "PG_DATABASE=postgres" ' +
                        '-p 5432:5432') { c ->
            sh 'java -version'
            sh 'mvn -version'
            //        sh 'mvn -ntp clean install -DskipTests'
            /* Wait until mysql service is up */
            sh 'while ! mysqladmin ping -h0.0.0.0 --silent; do sleep 1; done'
            /* Run some tests which require MySQL */
        }
    }
}
