# Live session runbook: todo app with sign up

Default plan is **projector only**: most students will not have a laptop. The
deck is trimmed to 33 slides for a 60-minute slot (eight extra slides are kept
in `slides.md` behind `hide: true`; delete that line to bring one back).

## The hour, minute by minute

| Clock | Minutes | Slides | What happens |
|---|---|---|---|
| 9:30 | 5 | Cover, five years, the jobs, what I build | Who you are. Hands-up poll: made a web page? written a Java class? used SQL? |
| 9:35 | 3 | You already have the pieces, today in five parts | Tie the hour to their Semester III and IV subjects. |
| 9:38 | 6 | Place order, six hops, halwa shop | The whole idea. Ask which hop scares them most. |
| 9:44 | 9 | Browser three things, what a server does, Spring Boot one file, why not a file, request is just text | One idea per slide. Do not read the code line by line; point at the two lines that matter. |
| 9:53 | 2 | Let's build one, install the tools once | Frame the demo. Tools slide is 60 seconds: "do this at home first". |
| 9:55 | 17 | Steps 1 to 5, run it, three things that break | Live on the projector from `demo/`. Type the controller and the React form; paste the rest. Let one error happen and read it aloud. |
| 10:12 | 7 | Now put it on the internet, server cost, DNS, last mile | Open https://todo.giftson.org on the projector, ask the room to open it on phones and sign up, show their rows in the database. |
| 10:19 | 6 | Plan by semester, fresher interview, things I believed | The career part. Keep it to the bold lines. |
| 10:25 | 5 | Free things, questions, thank you | Homework line, then Q&A until the coordinator stops you. |

If you are running late at 10:12, skip the server cost slide and the last mile
slide; DNS alone carries the point. If you are running early, un-hide "A week
in my job" and "Working with AI, honestly": they are the two best of the cut.

If laptops do appear, the "Laptops open" notes below still apply; budget 35
minutes for the build and drop the career slides to the homework line.

## The day before (done once; here is what was set up)

The server at `giftson` (65.108.241.19) already runs the shared `cyrino-develop`
stack, with Traefik on ports 80 and 443. The lab therefore runs **behind that
Traefik**, from `deploy/compose.traefik.yml`, and publishes no ports of its own.

1. The repo is cloned at `/root/fx-seminar-todo-app` on the server.
2. `deploy/.env` on the server holds `DB_PASSWORD` (generated) and `EDGE_NETWORK=cyrino-develop_default`.
3. `deploy/traefik-lab.yaml` was copied into the running Traefik container at
   `/etc/traefik/dynamic/lab.yaml`. Traefik reloads it live. **If anyone recreates
   the Traefik container (`docker compose up` in `~/cyrino-develop`), copy it again:**
   `docker cp deploy/traefik-lab.yaml cyrino-develop-traefik-1:/etc/traefik/dynamic/lab.yaml`
4. The stack was built and started: `cd /root/fx-seminar-todo-app/demo/deploy && docker compose -f compose.traefik.yml up -d --build`
5. **DNS, still to do in Cloudflare:** A record `todo` → `65.108.241.19`, DNS only
   (grey cloud). Traefik fetches the certificate within a minute of the name resolving.
6. Then open https://todo.giftson.org and sign up once. Check with `nslookup todo.giftson.org` first.
7. On your laptop: `cd demo/backend && ./mvnw -q package` once, so Maven's cache is warm and the live `spring-boot:run` starts in seconds.
8. `cd demo/frontend && pnpm install` for the same reason.
9. Check the college Wi-Fi lets you reach `start.spring.io` and `registry.npmjs.org`. If not, use your phone's hotspot, and skip the "download from start.spring.io" moment.

Useful on the server:

```
cd /root/fx-seminar-todo-app && git pull
cd demo/deploy && docker compose -f compose.traefik.yml up -d --build   # redeploy after a change
docker compose -f compose.traefik.yml logs -f backend                    # watch Spring start
docker compose -f compose.traefik.yml exec db psql -U todo -c 'select id, name, email from users;'
```

## Tools (students, 5 minutes, laptops mode only)

```
# Windows PowerShell
winget install jdx.mise
# Mac / Linux
curl https://mise.run | sh
# then in the project folder
mise install
java -version && node -v
```

SDKMAN is the alternative if someone already has it (`sdk install java 17-tem`). Do not have the room install both.

## Step 1: generate the back end (2 min)

start.spring.io → Maven, Java, Boot 4.0.x, group `in.fxec`, artifact `todo`, Java 17,
dependencies: Spring Web, Spring Data JPA, Validation, H2 Database. Generate, unzip, then:

```
cd todo
./mvnw spring-boot:run
```

Point at "Tomcat started on port 8080". Open `localhost:8080` and show the Whitelabel error page: alive, but nothing to say yet.

Add to `pom.xml` (password hashing only, not the login framework):

```xml
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-crypto</artifactId>
</dependency>
```

Add to `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:h2:mem:todo
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2
spring.jpa.show-sql=true
```

## Step 2: users table (4 min)

Type `user/User.java` and `user/UserRepository.java` from `backend/src/main/java/in/fxec/todo/user/`.
Say out loud: table named `users` not `user`; unique email; `passwordHash` not `password`; `token`.

## Step 3: sign up and log in (6 min)

Type `auth/AuthService.java`, then `auth/AuthController.java`. Then `ApiErrors.java` so errors come back as JSON.
Restart, then from a second terminal:

```
curl -X POST localhost:8080/api/auth/signup -H 'Content-Type: application/json' \
  -d '{"name":"Sathya","email":"s@fxec.edu","password":"secret123"}'
```

Show: 201 with a token. Run it again: 400 "already registered". Try password `abc`: 400 naming the field.
Open `localhost:8080/h2` (JDBC URL `jdbc:h2:mem:todo`, user `sa`, blank password), `SELECT * FROM USERS`, point at the hash.

## Step 4: todos (5 min)

Type `todo/Todo.java`, `todo/TodoRepository.java`, `todo/TodoController.java`. Restart.

```
TOKEN=<paste from signup>
curl localhost:8080/api/todos -H "Authorization: Bearer $TOKEN"            # []
curl -X POST localhost:8080/api/todos -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d '{"title":"Finish DS lab record"}'  # 201
curl localhost:8080/api/todos                                               # 401 please log in first
```

The 401 without a token is the teaching moment: the server does not trust anyone who cannot prove who they are.

## Step 5: React (8 min)

```
pnpm create vite frontend --template react     # or copy demo/frontend
cd frontend && pnpm install
```

Add the proxy to `vite.config.js`, then `src/api.js`, `src/App.jsx`, `src/AuthForm.jsx`, `src/TodoList.jsx`, `src/styles.css` from `demo/frontend/src/`.
`pnpm dev`, open `localhost:5173`, F12 → Network, sign up, add a todo. Show the requests and the status codes. Switch to terminal 1 and show the SQL lines Spring printed.

## Step 6: ship it (5 min, if the server was prepared)

On the projector: `ssh giftson`, `cd /root/fx-seminar-todo-app/demo/deploy`, `docker compose -f compose.traefik.yml ps` (already running), open the site, ask the room to open it on their phones and sign up. Then:

```
docker compose -f compose.traefik.yml exec db psql -U todo -c 'select id, name, email from users;'
```

Their names appear. That is the moment to go back to the DNS slide and explain how their phones found the server.

## If things go wrong

| Symptom | Do |
|---|---|
| `mvnw` hangs downloading | Wi-Fi. Switch to hotspot, or `cd demo/backend && ./mvnw spring-boot:run` which is cached. |
| Port 8080 in use | `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081` and change the Vite proxy. |
| CORS error in browser | Proxy missing in `vite.config.js`. The finished one is in `demo/frontend`. |
| 415 from curl | Missing `-H 'Content-Type: application/json'`. Point at it, this is slide "three things that break". |
| Site not reachable | `nslookup todo.giftson.org` first; then `docker logs cyrino-develop-traefik-1 --tail 50` and `docker compose -f compose.traefik.yml logs lab-web`. |
| No internet at all | Run everything locally, skip step 6, show the DNS slide as a story. |
