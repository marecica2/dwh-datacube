# DWH - Data warehouse [![pipeline status](https://gitlab.com/marecica2/dwh-datacube/badges/develop/pipeline.svg)](https://gitlab.com/marecica2/dwh/-/commits/develop) [![codecov](https://codecov.io/gh/marecica2/dwh-datacube/branch/develop/graph/badge.svg)](https://codecov.io/gh/marecica2/dwh-datacube)



Showcase app for following stack dealing with multi-tenancy, 
datawarehouse solution with microservice architecture with CI pipeline and automated deployemnt to kubernetes cluster
- Spring boot (Batch, Data, Rest, Repositories, Test)
- Postgres
- Redis
- ReactJS
- Maven
- Docker
- Kubernetes
- Travis 

### Local development
```bash
# start infrastructure (Redis, Postgres)
./dev stack-start 
# stop infrastructure
./dev stack-stop 
```

### Databases

Running database migrations

```bash
./dev migrations
./dev migrations-test
```

Usefull docker commands

```bash
docker ps # find your CONTAINER ID
docker exec -it <container_id> bash # run bash command inside that container
```
