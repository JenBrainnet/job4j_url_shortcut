# job4j_url_shortcut

![Build Status](https://github.com/JenBrainnet/job4j_url_shortcut/actions/workflows/maven.yml/badge.svg)

## Project Description

UrlShortCut is a training REST API for replacing long website links with short links that point to the shortcut service.
The project is developed as part of the Job4j course.

The main client is another website, not an end user.
For example, `job4j.ru` registers in UrlShortCut, receives generated credentials, authenticates with JWT, and sends long URLs for conversion.

Example:

```text
Original URL:
https://job4j.ru/profile/exercise/106/task-view/532

Generated code:
ZRUfdD2

Short redirect URL:
https://shortcut.example/redirect/ZRUfdD2
```

The registered website can show the short redirect URL to its users.
When an end user opens `/redirect/ZRUfdD2`, UrlShortCut finds the original URL and returns HTTP 302 Redirect to it.

## How It Works

Flow:

1. A website sends `POST /registration` with its domain.
2. UrlShortCut returns generated `login` and `password` for that website.
3. The website sends `POST /login` with these credentials and receives a JWT token.
4. The website sends `POST /convert` with a long URL and the JWT token.
5. UrlShortCut returns a unique short code.
6. The website publishes a link like `https://shortcut.example/redirect/{code}`.
7. An end user opens that link and receives HTTP 302 Redirect to the original URL.
8. UrlShortCut increments the redirect counter for that URL.

## API Overview

Public endpoints:

- `POST /registration` registers a website and returns generated credentials.
- `POST /login` authenticates a registered website and returns a JWT token.
- `GET /redirect/{code}` redirects to the original URL.

JWT-protected endpoints:

- `POST /convert` converts a long URL into a short code.
- `GET /statistic` returns redirect statistics for the authenticated website.

## Redirect Counter

The service counts how many times each shortened URL was opened.
The counter must be incremented in the database, not with Java read-modify-save logic.

Database update is used because an operation like `UPDATE shortcut_urls SET total = total + 1`
is atomic at the database level.
This helps avoid lost updates when several users open the same short link at the same time.

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
