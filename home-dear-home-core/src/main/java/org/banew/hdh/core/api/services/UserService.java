package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.core.api.users.forms.RegisterForm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<? extends User> register(RegisterForm registerForm);
    CompletableFuture<? extends User> login(LoginForm loginForm);
    User getCurrentUser();

    Optional<? extends User> availableLoginByPassword();
    CompletableFuture<? extends User> login(String password);

    void logout();
}