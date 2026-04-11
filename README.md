# Remote Configurations & Targeting Cloud Service

[![Test Coverage](https://codecov.io/github/andreydem0505/RemoteConfig/branch/master/graph/badge.svg?token=VZ8E3CHJ47)](https://codecov.io/github/andreydem0505/RemoteConfig)

### REST API service combining Remote Configurations (Feature-Toggles) and Targeting management

### [Client Library](https://github.com/andreydem0505/RemoteConfigJavaLib)

### [Unit Testing Report](https://andreydem0505.github.io/RemoteConfig/test/index.html)

### [Load Testing Report](https://andreydem0505.github.io/RemoteConfig/load_test_report.html)

## Tech Stack:

- App:
  - Java 25
  - Spring Boot
  - Jetty Server
  - Gradle
- Storages:
  - MongoDB
  - Redis Cache
- Testing:
  - k6
  - JUnit
  - Grafana
  - Prometheus
  - micrometer
  - jococo
  - Codecov

## Build & Test:

There are 3 Docker profiles:
- **production**
- **test**
- **load-test**

For the load testing use scripts in `bash` folder. You'll get report in `docs` directory. During the load test you can access:

- Grafana: http://localhost:3000

![](/export/grafana.jpeg)

you can download this [dashboard](https://github.com/andreydem0505/RemoteConfig/blob/master/export/dashboard.json) to import in Grafana.

- k6: http://localhost:5665

![](/export/k6.jpeg)

Profiling data is collected and stored in `snapshots/profile.jfr`

## Environment variables:

`.env:`

```dotenv
SPRING_DATA_MONGODB_URI=
JWT_SECRET=
RANDOM_GENERATOR_SEED=
REDIS_HOST=redis
REDIS_PORT=6379
```

`.env.grafana:`

```dotenv
GF_SECURITY_ADMIN_PASSWORD=
```

`.env.mongo:`

```dotenv
MONGO_INITDB_ROOT_USERNAME=
MONGO_INITDB_ROOT_PASSWORD=
MONGO_INITDB_DATABASE=
```

`.env.testing:`

```dotenv
SPRING_DATA_MONGODB_URI=
JWT_SECRET=
RANDOM_GENERATOR_SEED=
REDIS_HOST=redis-test
REDIS_PORT=6379
```
