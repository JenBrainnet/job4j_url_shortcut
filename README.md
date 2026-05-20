# job4j_url_shortcut

![Build Status](https://github.com/JenBrainnet/job4j_url_shortcut/actions/workflows/maven.yml/badge.svg)

## Project Description

UrlShortCut is a training REST API for replacing long website links with short generated codes.
The service allows websites to register, receive credentials, authenticate with JWT, convert URLs, redirect users by short code, and view redirect statistics.
Redirect counters are stored in the database and updated atomically to avoid lost updates during concurrent requests.
The project is developed as part of the Job4j course.

## Technology Stack

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Liquibase
- Maven
- Checkstyle
- Swagger / OpenAPI

## Environment Requirements

- Java 21
- Maven 3.8+
- PostgreSQL 14+

## Project Launch

Create the database:

```sql
CREATE DATABASE url_shortcut;
```

Run the application:

```bash
./mvnw spring-boot:run
```

Run tests:

```bash
./mvnw test
```

## Contacts

- GitHub: [JenBrainnet](https://github.com/JenBrainnet)
