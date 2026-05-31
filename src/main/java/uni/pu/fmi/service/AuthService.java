package uni.pu.fmi.service;

import uni.pu.fmi.exception.BusinessException;
import uni.pu.fmi.model.Role;
import uni.pu.fmi.model.User;
import uni.pu.fmi.repo.UserRepo;

public class AuthService {

    private final UserRepo userRepo;

    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User register(String name, String email, String password, Role role) {
        if (userRepo.existsByEmail(email)) {
            throw new BusinessException("Имейлът вече е регистриран");
        }
        User user = new User(0, name, email, hash(password), role);
        return userRepo.save(user);
    }

    public User login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Акаунтът не съществува"));
        if (!user.getPasswordHash().equals(hash(password))) {
            throw new BusinessException("Невалидна парола");
        }
        return user;
    }

    private String hash(String password) {
        return "hash:" + password;
    }
}
