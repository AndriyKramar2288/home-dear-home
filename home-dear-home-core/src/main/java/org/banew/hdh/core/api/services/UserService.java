package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.domen.UserInfo;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.runtime.forms.RegisterForm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<? extends UserInfo> register(RegisterForm registerForm);
    CompletableFuture<? extends UserInfo> login(LoginForm loginForm);
    CompletableFuture<String> saveAvatarImage(byte[] image, String fileName);
    UserInfo getCurrentUser();

    Optional<? extends UserInfo> availableLoginByPassword();
    CompletableFuture<? extends UserInfo> login(String password);

    void logout();
}