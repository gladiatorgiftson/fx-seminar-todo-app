# Modern Full Stack Development, guest lecture at FXEC

Slides and the live-demo code for the guest lecture to II CSE C, Francis Xavier
Engineering College, Tirunelveli, on 9 October 2026.

- Slides: https://fx-presentation.giftson.org (also `modern-full-stack-development.pdf` here)
- The app from the lecture, running: https://todo.giftson.org

## Students: build it yourself

Read **[`demo/STUDENT-GUIDE.md`](demo/STUDENT-GUIDE.md)**. The `starter` branch
has the empty scaffold; `main` has the finished app to compare against.

## Run the finished app on your laptop

```
mise install                                  # Java 17, Node 24, pnpm
cd demo/backend  && ./mvnw spring-boot:run    # terminal 1, http://localhost:8080
cd demo/frontend && pnpm install && pnpm dev  # terminal 2, http://localhost:5173
```

## What is where

- `slides.md`  the deck, built with [Slidev](https://sli.dev). `pnpm install && pnpm dev`, then open http://localhost:3030.
- `demo/backend/`  Spring Boot 4 on Java 17: users, sign up, log in, todos. H2 in memory while learning, PostgreSQL with the `prod` profile.
- `demo/frontend/`  React with Vite: sign-up and log-in form, todo list.
- `demo/deploy/`  Docker files to put it on a server, standalone with Caddy or behind an existing Traefik.
- `demo/STEPS.md`  the minute-by-minute plan of the live session.
