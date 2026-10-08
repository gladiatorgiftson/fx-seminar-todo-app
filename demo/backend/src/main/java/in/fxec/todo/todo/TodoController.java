package in.fxec.todo.todo;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import in.fxec.todo.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** List, add, tick and delete todos - always only your own. */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todos;
    private final AuthService auth;

    public TodoController(TodoRepository todos, AuthService auth) {
        this.todos = todos;
        this.auth = auth;
    }

    record NewTodo(@NotBlank String title) {}

    record TodoResponse(Long id, String title, boolean done) {
        static TodoResponse from(Todo t) {
            return new TodoResponse(t.getId(), t.getTitle(), t.isDone());
        }
    }

    @GetMapping
    public List<TodoResponse> mine(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        var me = auth.currentUser(authHeader);
        return todos.findByOwnerOrderByIdDesc(me).stream().map(TodoResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse add(@RequestHeader(value = "Authorization", required = false) String authHeader,
                            @Valid @RequestBody NewTodo body) {
        var me = auth.currentUser(authHeader);
        return TodoResponse.from(todos.save(new Todo(body.title(), me)));
    }

    @PatchMapping("/{id}/toggle")
    public TodoResponse toggle(@RequestHeader(value = "Authorization", required = false) String authHeader,
                               @PathVariable Long id) {
        var me = auth.currentUser(authHeader);
        var todo = todos.findById(id)
            .filter(t -> t.getOwner().getId().equals(me.getId()))
            .orElseThrow(() -> new IllegalStateException("No such todo"));
        todo.setDone(!todo.isDone());
        return TodoResponse.from(todos.save(todo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(value = "Authorization", required = false) String authHeader,
                       @PathVariable Long id) {
        var me = auth.currentUser(authHeader);
        todos.findById(id)
            .filter(t -> t.getOwner().getId().equals(me.getId()))
            .ifPresent(todos::delete);
    }
}
