# Build the todo app yourself

This is the app from the lecture: sign up, log in, and a todo list that is
yours alone. `main` has the finished version. The `starter` branch has only
the empty scaffold, so you can write the real parts with your own hands.

Budget: one weekend. Two to three hours per part. Stop when you are tired;
the branch will wait.

## 0. Tools, once

```
# Windows (PowerShell)        # Mac or Linux
winget install jdx.mise       curl https://mise.run | sh
```

Then:

```
git clone https://github.com/gladiatorgiftson/fx-seminar-todo-app.git
cd fx-seminar-todo-app
mise install            # installs Java 17, Node 24 and pnpm, pinned in mise.toml
git checkout starter    # the scaffold, nothing else
```

Check: `java -version` says 17, `node -v` says 24.

## 1. See the empty server run

```
cd demo/backend
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

Wait for `Started TodoApplication`. Open http://localhost:8080 in a browser.
The error page is Spring saying "I am alive, but you have not told me what to
answer yet." Press Ctrl+C to stop it.

## 2. The users table

Create `src/main/java/in/fxec/todo/user/User.java` and `UserRepository.java`.
A user has an id, a unique email, a name, a password *hash*, and a token.

Rules to keep: the table is called `users` (not `user`, a reserved word), the
email is unique, and you never store the password itself.

Stuck? Look at the finished file without leaving your branch:

```
git show main:demo/backend/src/main/java/in/fxec/todo/user/User.java
```

## 3. Sign up and log in

Create `auth/AuthService.java` and `auth/AuthController.java`, then
`ApiErrors.java` beside `TodoApplication.java` so errors come back as JSON.

- Sign up: refuse a duplicate email, hash the password with `BCryptPasswordEncoder`,
  make a token with `UUID.randomUUID()`, save.
- Log in: find by email, check the password against the hash, hand out a new token.
- Same error message for "no such email" and "wrong password".

Test with curl before touching any UI:

```
curl -X POST localhost:8080/api/auth/signup -H "Content-Type: application/json" \
  -d "{\"name\":\"You\",\"email\":\"you@example.com\",\"password\":\"secret123\"}"
```

You should get `201` and a JSON with a token. Run it again: `400`, already
registered. Open http://localhost:8080/h2 (JDBC URL `jdbc:h2:mem:todo`, user
`sa`, no password) and run `SELECT * FROM USERS`. Look at the hash.

## 4. Todos, and only yours

Create `todo/Todo.java`, `todo/TodoRepository.java`, `todo/TodoController.java`.
A todo has an id, a title, a `done` flag, and an owner (`@ManyToOne User`).

Every endpoint starts by turning the `Authorization: Bearer <token>` header
into a user. No token, no list: reply `401`.

```
TOKEN=paste-it-here
curl localhost:8080/api/todos -H "Authorization: Bearer $TOKEN"          # []
curl -X POST localhost:8080/api/todos -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d "{\"title\":\"First one\"}"     # 201
curl localhost:8080/api/todos                                             # 401
```

## 5. The React side

```
cd demo/frontend
pnpm install
pnpm dev                 # http://localhost:5173
```

`src/api.js` and `src/styles.css` are already there: the boring parts. Write:

- `src/App.jsx`: if there is a saved user in `localStorage`, show the list; otherwise the form.
- `src/AuthForm.jsx`: name, email, password; on submit call `api('/auth/signup', ...)`.
- `src/TodoList.jsx`: load `/todos` when it appears; add, tick, delete through the API.

Keep the backend running in another terminal. Press F12, open the Network tab,
and watch every click become a request with a status code.

## 6. When it breaks

| You see | It means | Do |
|---|---|---|
| `blocked by CORS policy` | The browser on 5173 will not call 8080 directly. | The proxy in `vite.config.js` must point at 8080. |
| `415` or `400` from Spring | Missing `Content-Type: application/json`, or a key does not match a field name. | Read the response body. Spring names the field. |
| `Table "USERS" not found` | The entity and the table disagree. | Check `@Table(name = "users")` and restart. |
| `401 Please log in first` | No token or an old one. | Sign up again, copy the new token. |

The first line of the error is the answer nine times out of ten. Read it to the end before changing anything.

## 7. Compare, then show someone

```
git diff starter main --stat          # what the finished version has that you do not
git diff starter main -- demo/backend/src/main/java/in/fxec/todo/todo/TodoController.java
```

Then put it on the internet. `demo/deploy/compose.yml` with Docker on any
small server, or a free tier from https://free-for.dev. A URL beats a zip file
in every interview.
