## Awesome blog project
Create posts, comments to posts and like them!

- [Tech stack](#Tech-stack)
- [Known issues](#Known-issues)
- [Rules for contributors](#Rules-for-contributors)
- [Architecture](#Architecture)
    - [Future concerns](#Future-concerns)
- [Project structure](#Project-structure)
- [Useful commands](#Usefull-commands)

## Tech stack
Language: `Java 21`

Framework: `Spring core + Spring boot`

Test tools: `Spring boot test \ JUNIT 5 \ Jupiter \ Mockito`

Build tools: `Maven`

HTML templates: `Thymeleaf` 

Database driver: `JDBC`

Observability: `Actuator`

## Known issues
Visit, please, `ISSUES.md` file in the project root for problems list.

## Rules for contributors:
We're using:
- conventional-commits;
- folder structure convention;
- avoiding GOD files.

We're respecting:
- SOLID;
- DRY;
- KISS;
- YAGNI;
- and other good principles.

## Architecture
Project use `MVC` architecture

### Future concerns:
- Migrate to `Hexagonal architecture`;
- Moving to `SPA` instead of templates.

## Project structure
- configuration - for create Beans of infrastructure classes
- controllers - for HTTP requests
- models - as business model unit
- services - as business models orchestrator
- repositories - as layer for database encapsulation
- resources/templates - view templates

## Usefull commands

Run unit tests: `./mvnw test`

Run integration tests: `./mvnw verify`

Build jar: `./mvnw clean install`

## Actuator endpoints
Opened actuator endpoints list:
- health
- info
- metrics

Example: `localhost:8080/actuator/metrics`
