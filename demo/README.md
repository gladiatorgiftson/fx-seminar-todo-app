# Todo with sign up: the lecture's demo app

- `backend/`  Spring Boot 4, Java 17, Maven. Users, sign up, log in, todos. H2 in memory by default, PostgreSQL with the `prod` profile.
- `frontend/` React 19 with Vite. Sign-up and log-in form, todo list.
- `deploy/`   Docker Compose with PostgreSQL, the back end, and Caddy serving the front end with automatic HTTPS.
- `STEPS.md`  the minute-by-minute runbook for the live session.

Run locally:

```
mise install                       # Java 17, Node 24, pnpm
cd backend && ./mvnw spring-boot:run
cd frontend && pnpm install && pnpm dev     # http://localhost:5173
```

Deploy on a server with Docker:

```
cd deploy && cp .env.example .env   # set SITE_ADDRESS and DB_PASSWORD
docker compose up -d --build
```
