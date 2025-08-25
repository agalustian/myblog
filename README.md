## Awesome blog project
Create posts, comments to posts and like them!

- [Tech stack](#Tech-stack)
- [Known issues](#Known-issues)
- [Rules for contributors](#Rules-for-contributors)
- [Architecture](#Architecture)
    - [Future concerns](#Future-concerns)
- [Project structure](#Project-structure)
- [Useful commands](#Usefull-commands)

## `!NOTE` Project don't use `spring-boot` in current project, until time good times will come.

## Tech stack
Language: `Java 21`

Dep.Injection: `Spring core`

Test tools: `Spring test context \ JUNIT 5 \ Jupiter \ Mockito`

Build tools: `Gradle`

HTML templates: `Thymeleaf` 

Database driver: `JDBC`

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
- Using `spring-boot`;
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

Run tests: `./gradlew test`

build war: `./gradlew war`

After war building copy `.war` from `./app/target/myblog/myblog.war` to your http server (tomcat)
command example: `cd myblog && ./gradlew war && cd .. && cp ./myblog/app/target/myblog/myblog.war ./apache-tomcat-10.1.41/webapps/myblog.war && sh ./apache-tomcat-10.1.41/bin/catalina.sh stop && sleep 5  && sh ./apache-tomcat-10.1.41/bin/catalina.sh jpda start`