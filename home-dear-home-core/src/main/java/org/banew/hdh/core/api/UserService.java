package org.banew.hdh.core.api;

import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.core.api.users.forms.RegisterForm;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<User> register(RegisterForm registerForm);
    CompletableFuture<User> login(LoginForm loginForm);
    User getCurrentUser();
}