# Swish

Basketball shot-logging REST API (one microservice that owns its data).
Java 21, Spring Boot 4.1, Spring Data JPA, PostgreSQL, Bean Validation, Actuator, Maven.
Base path: `/swish/api/v1/shots` (GET all, GET by id, POST, PATCH, DELETE).

## Start of every session
- Before starting work, briefly summarize "Current status" and "Next steps" below
  and ask the user to confirm. Plans may have changed since this file was written
  (the user works across two machines).
- When a decision is made or a step is finished, update this file so it doesn't go stale.

## How to work in this repo
- The developer is learning and writes the code themselves. Guide with hints and
  small steps, then review. Don't write large chunks of code unasked.
- Build: `./mvnw compile` (on Windows use `.\mvnw.cmd`). Tests: `./mvnw test`.

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

## Current status (2026-10-30)
Exception handler written and compiling; not tested yet.

## Next steps, in order
1. **Spring Security:** the starter is on the classpath with no config, so every
   request gets 401. Add a config class (permit all for now; real auth comes later).
2. **Manually test the exception handler.** Send bad requests, watch for 500s.
   Try `Content-Type: text/plain` on POST; it should reveal a missing handler (415).
3. **Enum casing bug:** `ShotsMapper.toEntity` uppercases, but `ValueOfEnumValidator`
   and `ShotsServiceImpl.updateShots` don't. Lowercase category/location in PATCH makes
   `Category.valueOf` throw → 500. Fix in the service, not the handler.
4. POST returns 200; should return 201 Created + a Location header.
5. DELETE on a missing id silently succeeds (GET throws 404). Decide the behavior.
6. Cleanups: unused imports (`HttpResponse` in the controller, `Bean` in the service
   impl), stray `;` in the mapper, no pagination on `findAll()`.

Then: tests (service unit tests with Mockito, `@WebMvcTest`, integration) → logging →
Docker + Compose → Redis caching → CI/CD → AWS deploy → alarms → auth.

## Later (optional improvements)
- `Shots.checkIfValid()` stops at the first broken rule. It could collect all of them,
  with `InvalidShotsException` carrying a `List<String>`.
- The bad-JSON handler could name the field with the wrong type by inspecting
  `ex.getCause()` (the Jackson exception). Never expose the raw message.
