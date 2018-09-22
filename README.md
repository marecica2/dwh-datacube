# Local development
Copy the values of `.env.sample` to your `.env` file.

### Running Docker Containers

*Docker for Mac: Please increase your VM memory limit to at least 3GB*

Pull all docker images and build app container

```bash
mvn integration-test
```



```bash
scripts/dev up -d
scripts/dev # you can check if everything is running as expected
```

How to run some custom commands using docker

```bash
scripts/dev yarn install # it run yarn command in separate app container
scripts/dev exec yarn install # it run yarn command inside running app container
scripts/dev exec bash # it will spawn new bash shell inside running app container and then you can run yarn install
```

### Databases

Populating database from db dump

```bash
# MySQL
scripts/dev db-restore-mysql .db/seeds/mysql_small.sql.gz 
# PostgreSQL
scripts/dev db-restore tenant1 .db/seeds/postgres_tenant1_20180621_1159.sql.gz
```
Creating database dumps

```bash
# PostgreSQL public schema for specified database
scripts/dev db-dump tenant1
# MySQL specified databases list (space separated)
scripts/dev db-dump-mysql cas_tenant complexity_access
```
Running the Sequelize migrations

```bash
scripts/dev yarn migrations
```

How to access database container directly

```bash
docker ps # find your CONTAINER ID
docker exec -it <container_id> bash # run bash command inside that container
```

### Access app

1. Point browser to localhost:3000/complexity-dev/. Now the xray should be loaded with the login screen. Log in using some tenant user.

2. After logging in, point your browser to http://localhost:3000/complexity-dev/myprojects/ and make sure the page is reloaded. This will switch to proxy the cas frontend with preserved user session.

### Access database from localhost

Sometime you may want to access database directly from your machine instead use cli interface in docker container. All 
databases publishes ports on your local machine. You can check specific port by looking at `docker-compose.yml` file. For 
example PostgresSQL exposes `30001`.

### Frontend

@TODO

```bash
scripts/dev exec yarn build:watch # run it directly inside working app
```

### Other useful commands

```bash
scripts/dev build # build all containers that can be build
scripts/dev pull # pull latest image versions from docker repository
scripts/dev logs # combined logs from all services
scripts/dev logs app # logs for app container
scripts/dev stop # stop all containers
scripts/dev rm # remove all containers - it won't remove volumes
scripts/dev rm db-xray # remove single container
scripts/dev clean # remove all volumes that can be removed to free disk space (WARNING: it will erase all data)
``` 


