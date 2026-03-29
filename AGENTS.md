# AGENTS.md — TMS Coding Agent Guide

## Project Overview
**Translation Management System** — a Spring Boot 1.5.7 + JSF (Mojarra 2.2.14) + PrimeFaces 6.0 web app.
Packaged as a WAR but runs embedded via `spring-boot-maven-plugin`. H2 file-based database (no MySQL in dev).
Compiled and run on **Java 25** with a mandatory `--add-opens` workaround (see below).

## Build & Run

```bash
# Compile
mvn compile -f pom.xml

# Run (embedded Tomcat on port 8081)
mvn spring-boot:run

# Or run TmsApplication.main() directly in IntelliJ — it auto-relaunches with --add-opens
```

**App URL:** `http://localhost:8081/tms`

### Java 25 `--add-opens` Workaround
`TmsApplication.main()` detects whether CGLIB reflection works; if not, it **relaunches itself** as a subprocess with the required `--add-opens` flags (defined in `pom.xml` under `<jvm.add.opens>`). Do not remove this logic.

## Architecture

```
com.tms/
  TmsApplication.java              ← @SpringBootApplication entry point
  controller/                      ← JSF backing beans (@Component + JSF scope)
  model/
    entity/                        ← JPA entities
      service/                     ← @Service / @Transactional business logic
      webservice/                  ← ApplicantService servlet (POST /aws)
      data/                        ← CustumResult (query result DTO)
  repository/                      ← Spring Data JPA repositories
  util/
    dataEntity/DefaultDataLoader   ← Seeds all reference data on first login
    crypt/                         ← MD5 password hashing
    message/                       ← JSF FacesMessage helpers
```

**Data flow:** JSF XHTML → `controller/` (backing bean) → `model/entity/service/` → `repository/` → H2.

## Entity Conventions

- **Most entities extend `CoreEntity`** which provides `Integer id` with `@GeneratedValue(GenerationType.AUTO)`. Do NOT add a duplicate `@Id` in subclasses.
- **Exceptions** — `Project`, `Translator`, `User`, `Language` define their own `@Id` with `GenerationType.IDENTITY` (not extending `CoreEntity`).
- `User` maps to table **`app_user`** (not `user`) — `user` is a reserved word in H2.
- `@OneToMany` collections must use `FetchType.LAZY` or be typed as `Set<>` — never use multiple `List` with eager fetch on the same entity (causes `MultipleBagFetchException`).
- `Country` (and other `CoreEntity` subclasses) rely on H2 auto-sequence for `GenerationType.AUTO`; do not set `id` manually before `save()`.

## Repository Pattern

All repositories extend `AbstractRepository<T, ID>` (which extends `JpaRepository`):

```java
// Example — CountryRepository.java
@Repository
public interface CountryRepository extends AbstractRepository<Country, Integer> {
    List<Country> findByName(final String name);
}
```

Custom finders use Spring Data method naming. For lookup-or-throw helpers, add a `default` method (see `RoleRepository.find(String name)`).

## Services

Located in `com.tms.model.entity.service`. Annotated `@Service` (or `@Component`) and `@Transactional`. May mix repository calls with direct `@PersistenceContext EntityManager` for complex dynamic queries (see `ClientService.search()`).

## JSF Controllers (Backing Beans)

Annotated `@Component` + a JSF scope (`@SessionScoped`, `@ViewScoped`). Navigation returns a `String` outcome name matching a `.xhtml` filename under `src/main/webapp/`. Use `AbstractController<T>` for CRUD form caption helpers.

## Reference Data Loading

`DefaultDataLoader.updateAllEntitties()` is called on every login (via `LoginController.login()`). It checks existence before inserting to be idempotent. All persistence goes through repositories — no `EntityManager` calls inside `DefaultDataLoader`.

## Key Configuration (`application.properties`)

| Property | Value |
|---|---|
| `server.port` | `8081` |
| `server.context-path` | `/tms` |
| `spring.datasource.url` | `jdbc:h2:file:./data/tmsdb` |
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `jsf.primefaces.theme` | `redmond` |

## Known Pitfalls
- Do not rename `app_user` back to `user` — H2 syntax error.
- `spring.main.allow-bean-definition-overriding=true` is required (JSF + Spring Boot bean conflicts).
- `TmsApplication` carries `@EnableAutoConfiguration`, `@SpringBootApplication`, and `@ComponentScan` — do not add conflicting `@Configuration` classes that re-declare datasource or JPA beans.
- `SelectItemsUtils` in `com.tms.converter` replaces the removed `com.sun.faces.renderkit.SelectItemsIterator` — do not reintroduce that internal class.

