cp settings.xml $HOME/.m2/settings.xml
mvn -ntp deploy -DskipTests

docker build -t marecica2/dwh-migrator -t marecica2/dwh-migrator:$SHA -f ./migrator/Dockerfile ./migrator
docker build -t marecica2/dwh-importer -t marecica2/dwh-migrator:$SHA -f ./importer/Dockerfile ./importer
docker build -t marecica2/dwh-react-app -t marecica2/dwh-migrator:$SHA -f ./react-app/Dockerfile ./react-app

echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_ID" --password-stdin

docker push marecica2/dwh-migrator
docker push marecica2/dwh-importer
docker push marecica2/dwh-react-app