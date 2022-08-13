# Project 59

Spring Boot JobRunr

[https://gitorko.github.io/spring-jobrunr/](https://gitorko.github.io/spring-jobrunr/)

### Version

Check version

```bash
$java --version
openjdk 17.0.3 2022-04-19 LTS
```

### Postgres DB

```
docker run -p 5432:5432 --name pg-container -e POSTGRES_PASSWORD=password -d postgres:9.6.10
docker ps
docker exec -it pg-container psql -U postgres -W postgres
CREATE USER test WITH PASSWORD 'test@123';
CREATE DATABASE "test-db" WITH OWNER "test" ENCODING UTF8 TEMPLATE template0;
grant all PRIVILEGES ON DATABASE "test-db" to test;

docker stop pg-container
docker start pg-container
```

### Dev

To run the code

```bash
./gradlew clean build
./gradlew bootRun
```

```bash
curl --location --request GET 'http://localhost:8080/api/500'
```
