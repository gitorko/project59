# Project 59

Spring Boot JobRunr

[https://gitorko.github.io/](https://gitorko.github.io/)

## Setup

### Postgres DB

```
docker run -p 5432:5432 --name pg-container -e POSTGRES_PASSWORD=password -d postgres:9.6.10
docker ps
docker run -it --rm --link pg-container:postgres postgres:9.6.10 psql -h postgres -U postgres
CREATE USER test WITH PASSWORD 'test@123';
CREATE DATABASE "test-db" WITH OWNER "test" ENCODING UTF8 TEMPLATE template0;
grant all PRIVILEGES ON DATABASE "test-db" to test;
```

### Dev

To run the backend in dev mode Postgres DB is needed to run the integration tests during build.

```bash
./gradlew clean build
./gradlew bootRun
```
