package uni.pu.fmi.repo;

import uni.pu.fmi.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepo {
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    public User save(User user) {
        if (user.getId() == 0) {
            user.setId(nextId++);
        }
        users.add(user);
        return user;
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public void clear() {
        users.clear();
        nextId = 1;
    }
}
