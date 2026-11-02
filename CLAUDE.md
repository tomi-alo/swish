# Swish

Basketball shot-logging REST API (one microservice that owns its data).
Java 21, Spring Boot 4.1, Spring Data JPA, PostgreSQL, Bean Validation, Actuator, Maven.
Base path: `/swish/api/v1/shots` (GET all, GET by id, POST, PATCH, DELETE).

## Progress notes
The session format (start-of-session quiz, recaps), concepts to revisit, current status
and next steps live in `OneDrive/claude/swish-progress.md` (synced between PC and laptop).
Read it at the start of every session and keep progress there, not here.
- This file holds stable project facts. When a design decision is made, update
  "Decisions made" below. Commit changes to this file only after the user says go ahead.

## How to work in this repo
- The developer is learning and writes the code themselves. Guide with hints and
  small steps, then review. Don't write large chunks of code unasked.
- Build: `./mvnw compile` (on Windows use `.\mvnw.cmd`). Tests: `./mvnw test`.
- Java 26 in-process javac can crash with `"this.hashes" is null`, which hides real
  compile errors; run `mvnw compile -Dmaven.compiler.fork=true` to see the real error.
- Local runs need env vars `SWISH_SECURITY_USERNAME`, `SWISH_SECURITY_PASSWORD`,
  `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (no defaults; the app
  won't start without them).

## Decisions made
- **Error format:** custom `ErrorResponse(statusCode, errors, message)` record, not
  Spring's ProblemDetail. Goal: every error has the same shape. `errors` is always a
  list (never null); `message` is a short headline.
- **Exception handling:** `exception/GlobalExceptionHandler.java` (`@RestControllerAdvice`)
  - ShotsNotFoundException → 404
  - InvalidShotsException → 400
  - MethodArgumentNotValidException → 400 (one entry per bad field)
  - HttpMessageNotReadableException → 400 (generic text, never raw `ex.getMessage()`)
  - MethodArgumentTypeMismatchException → 400
  - NoResourceFoundException → 404, HttpRequestMethodNotSupportedException → 405
  - Exception (catch-all) → 500 with a generic message; real error logged via SLF4J
  - A client mistake that shows up as a 500 in the logs = a missing handler.
- **Enum input is lenient:** category/location accept any case and surrounding spaces.
  The rule (trim + `equalsIgnoreCase`, no `toUpperCase`, which is locale-dependent) lives in
  `Category.fromString` / `Location.fromString`, used by the mapper and service.
  `ValueOfEnumValidator` follows the same rule; keep the two in sync.
- **Security (for now):** `config/SecurityConfig.java`. CSRF disabled (stateless API, no
  cookie login). GET is public; POST/PATCH/DELETE need HTTP Basic with one user. Later, at
  the "auth" step, replace Basic with JWT (or OAuth2 via e.g. Cognito) to learn it.
  - Gotcha: a request carrying WRONG Basic credentials gets 401 even on a public GET
    (the login filter runs before the rules). Postman: use "No Auth" for public requests.
- **Deployment plan:** build locally with Docker Compose first, deploy to AWS around
  mid-December 2026.
  - EC2 (1 GB) runs two containers: Swish (`mem_limit: 450m`, `-XX:MaxRAMPercentage=75`,
    Tomcat threads ~50, Hikari pool ~5) and Redis (`mem_limit: 64m`, `maxmemory 48mb`,
    `allkeys-lru`). Add a 1–2 GB swap file; tune with `docker stats`.
  - Postgres on RDS (`db.t4g.micro`, single-AZ), not public; port 5432 open only to the
    EC2 security group. Swish itself is public.
  - Add Swagger UI (springdoc-openapi) so people can try the API.

## Not decided yet (don't treat as settled)
- Logging + alarms (SLF4J/Logback basics covered; CloudWatch not decided)
- Gateway: AWS API Gateway vs an nginx container
- Custom domain vs the default AWS URL
