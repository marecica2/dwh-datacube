# #cp settings.xml $HOME/.m2/settings.xml
mvn -ntp install -DskipTests

export SHA=$(git rev-parse HEAD)

docker build -t marecica2/dwh-web-dwh -t marecica2/dwh-web-dwh:$SHA -f ./web-dwh/Dockerfile ./web-dwh
docker build -t marecica2/dwh-migrator -t marecica2/dwh-migrator:$SHA -f ./migrator/Dockerfile ./app-migrator
docker build -t marecica2/dwh-olap -t marecica2/dwh-migrator:$SHA -f ./migrator/Dockerfile ./app-olap
docker build -t marecica2/dwh-importer -t marecica2/dwh-importer:$SHA -f ./importer/Dockerfile ./app-importer

echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_ID" --password-stdin

#docker push marecica2/dwh-migrator
#docker push marecica2/dwh-importer
#docker push marecica2/dwh-olap
#docker push marecica2/dwh-web-dwh
docker push marecica2/dwh-web-dwh:$SHA
docker push marecica2/dwh-migrator:$SHA
docker push marecica2/dwh-importer:$SHA
docker push marecica2/dwh-olap:$SHA

kubectl apply -f .k8s

kubectl set image deployments/web-dwh-deployment client=marecica2/dwh-web-dwh:$SHA
kubectl set image pods/migrator-pod migrator=marecica2/dwh-migrator:$SHA
kubectl set image deployments/importer-deployment importer=marecica2/dwh-importer:$SHA
kubectl set image deployments/olap-deployment importer=marecica2/dwh-olap:$SHA
