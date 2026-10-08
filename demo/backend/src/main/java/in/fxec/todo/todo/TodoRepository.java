package in.fxec.todo.todo;

import java.util.List;
import in.fxec.todo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByOwnerOrderByIdDesc(User owner);
}
