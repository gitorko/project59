# Project 59

Spring Boot JobRunr

[https://gitorko.github.io/spring-jobrunr/](https://gitorko.github.io/spring-jobrunr/)

### Version

Check version

```bash
$java --version
openjdk version "21.0.3" 2024-04-16 LTS
```

### Postgres DB

```
docker run -p 5432:5432 --name pg-container -e POSTGRES_PASSWORD=password -d postgres:14
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

Dashboard:

[http://localhost:8085/dashboard](http://localhost:8085/dashboard)

```bash
curl --location --request GET 'http://localhost:8080/invoke-job'
curl --location --request GET 'http://localhost:8080/direct-call'
curl --location --request GET 'http://localhost:8080/retry-job'

```

Create a pool of 3 servers

```bash
java -jar build/libs/project59-1.0.0.jar --server.port=8081 --org.jobrunr.dashboard.enabled=true
java -jar build/libs/project59-1.0.0.jar --server.port=8082 --org.jobrunr.dashboard.enabled=false
java -jar build/libs/project59-1.0.0.jar --server.port=8083 --org.jobrunr.dashboard.enabled=false
```


Submit 500 jobs that will be processed by the 3 servers

```bash
curl --location --request GET 'http://localhost:8081/many-job/500'
```
