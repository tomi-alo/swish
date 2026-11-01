# Swish

Basketball shot-logging REST API (one microservice that owns its data).
Java 21, Spring Boot 4.1, Spring Data JPA, PostgreSQL, Bean Validation, Actuator, Maven.
Base path: `/swish/api/v1/shots` (GET all, GET by id, POST, PATCH, DELETE).

## Start of every session
1. **Quiz first** (before the status summary, which would give answers away):
   - Read recent history: `git log -8 --stat`, plus diffs of the code commits
     (skip notes-only commits like "Update project notes").
   - Ask 3–5 questions one at a time, in chat. Focus on *why* and concepts, not syntax.
     Include topics from "Concepts to revisit" below.
   - After each answer: what's right, what's missing, a plain-words correction.
   - Keep it to ~5 minutes. The user can say "skip".
2. Then briefly summarize "Current status" and "Next steps" below and ask the user to
   confirm. Plans may have changed since this file was written (the user works across
   two machines).
- When a decision is made or a step is finished, update this file so it doesn't go stale.
  You may commit changes to this file, but only after the user says go ahead.

## Concepts to revisit
Add a topic when a quiz answer shows a gap; remove it once answered well.
- Hashing vs encoding (`encoder.encode()` actually hashes; Base64 is reversible encoding)
- JWT and HTTPS are separate layers (HTTPS = the tunnel; Basic/JWT = what goes inside)

## How to work in this repo
- The developer is learning and writes the code themselves. Guide with hints and
  small steps, then review. Don't write large chunks of code unasked.
- After each major step: give a recap with questions, after testing and before committing.
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
- **Enum input is lenient:** category/location accept any case and surrounding spaces.
  The rule (trim + `equalsIgnoreCase`, no `toUpperCase`, which is locale-dependent) lives in
  `Category.fromString` / `Location.fromString`, used by the mapper and service.
  `ValueOfEnumValidator` follows the same rule; keep the two in sync.
- **Security (for now):** `config/SecurityConfig.java`. CSRF disabled (stateless API, no
  cookie login). GET is public; POST/PATCH/DELETE need HTTP Basic with one user. Later, at
  the "auth" step, replace Basic with JWT (or OAuth2 via e.g. Cognito) to learn it.
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

## Current status (2026-11-01)
Spring Security done and tested in Postman (see "Decisions made"). Exception handler
written but not systematically tested. Local runs need env vars `SWISH_SECURITY_USERNAME`,
`SWISH_SECURITY_PASSWORD`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
(no defaults; the app won't start without them).

Known gotcha: a request carrying WRONG Basic credentials gets 401 even on a public GET
(the login filter runs before the rules). Postman: use "No Auth" for public requests.
Java 26 in-process javac can crash with `"this.hashes" is null`, which hides real
compile errors; run `mvnw compile -Dmaven.compiler.fork=true` to see the real error.

## Next steps, in order
1. ~~Spring Security~~ (done 2026-11-01).
2. **Manually test the exception handler.** Send bad requests, watch for 500s.
   Try `Content-Type: text/plain` on POST; it should reveal a missing handler (415).
3. ~~Enum casing bug~~ (done 2026-10-31, see "Decisions made"). Unit tested
   (`CategoryTest`, `LocationTest`) and confirmed with real requests.
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
