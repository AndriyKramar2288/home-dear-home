package org.banew.hdh.core.api.users;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<User> register(RegisterForm registerForm);
    CompletableFuture<User> login(String login, String password);
    Optional<User> getCurrentUser();
}