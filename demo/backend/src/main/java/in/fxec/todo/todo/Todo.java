package in.fxec.todo.todo;

import jakarta.persistence.*;
import in.fxec.todo.user.User;

/** One row in the "todo" table. Every todo belongs to exactly one user. */
@Entity
public class Todo {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private boolean done = false;

    @ManyToOne(optional = false)
    private User owner;

    protected Todo() {}

    public Todo(String title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public User getOwner() { return owner; }
}
