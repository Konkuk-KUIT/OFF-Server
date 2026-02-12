# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew build          # Build the project
./gradlew test           # Run all tests
./gradlew bootRun        # Run Spring Boot application
./gradlew clean build    # Clean and rebuild
```

Java 21 is required. The project uses Gradle with Spring Boot 3.5.9.

## Required Environment Variables

`DB_ENDPOINT`, `DB_USER`, `DB_PASSWORD` (PostgreSQL), `GEMINI_API_KEY`, `JWT_SECRET`, `PAYMENT_CLIENT_KEY`, `PAYMENT_SECRET_KEY`

## Architecture

Spring Boot application following **layered + domain-driven design**. Base package: `com.example.off`

### Package Structure

- **`domain/`** — Each domain (member, project, chat, pay, notification, task, partnerRecruit, projectMember, role) has its own entity, controller, service, repository, and dto sub-packages.
- **`common/`** — Cross-cutting concerns: exception handling, JWT auth, Gemini LLM integration, Toss Payments client, WebSocket config, Swagger config, response wrappers.
- **`config/`** — Top-level config (PasswordConfig, FilterConfig).
- **`jwt/`** — JwtTokenProvider and JwtAuthenticationFilter.

### Key Patterns

**Unified response format**: All endpoints return `BaseResponse<T>` with `success`, `code`, `message`, `data` fields.

**Exception handling**: Throw `OffException(ResponseCode.SOME_CODE)` — caught by `OffControllerAdvice` and mapped to the appropriate HTTP status via the `ResponseCode` enum.

**Entity conventions**:
- `@Getter @NoArgsConstructor @Entity` with private constructors
- Static factory methods (`Member.of(...)`, `PayLog.ready(...)`) instead of public constructors
- `@CreationTimestamp`/`@UpdateTimestamp` for audit fields
- `FetchType.LAZY` for all relationships
- Business logic methods directly on entities (e.g., `member.updateNickname()`)

**DTO conventions**:
- Request DTOs: Lombok `@Getter @AllArgsConstructor @NoArgsConstructor` with Jakarta validation (`@NotNull`, `@NotBlank`)
- Response DTOs: static `of()` factory methods to convert from entities
- Some DTOs use Java records for simple immutable types
- `@JsonFormat(pattern = "yyyy-MM-dd")` for LocalDate fields

**Service layer**: `@Transactional` for writes, `@Transactional(readOnly = true)` for reads. Dependencies injected via `@RequiredArgsConstructor`.

**Authentication**: JWT tokens extracted in filter, `memberId` available via `req.getAttribute("memberId")` in controllers. WebSocket auth uses STOMP interceptor with JWT from headers.

### External Integrations

- **Gemini API** (`common/gemini/`): LLM-based project estimation via WebClient
- **Toss Payments** (`common/infra/TossPaymentsClient`): Payment confirmation via WebClient with Basic auth
- **WebSocket/STOMP** (`common/config/WebSocketConfig`): Real-time chat with `/pub`, `/sub`, `/queue`, `/user` destinations

### Database

PostgreSQL with Hibernate `ddl-auto: update`. Initial data loaded from `src/main/resources/data.sql`.