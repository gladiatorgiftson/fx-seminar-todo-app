package in.fxec.todo.user;

import jakarta.persistence.*;

/** One row in the "users" table. Same idea as a class in your OOP lab - with an id. */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    /** Never the real password. Only a hash of it. */
    private String passwordHash;

    /** Random string we hand out after login. The front end sends it back with every request. */
    private String token;

    protected User() {}

    public User(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPasswordHash() { return passwordHash; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
