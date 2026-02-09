package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.dto.DetailedUserInfo;
import org.banew.hdh.core.api.dto.UserInfo;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.runtime.forms.RegisterForm;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<? extends DetailedUserInfo> register(RegisterForm registerForm);

    CompletableFuture<? extends DetailedUserInfo> login(LoginForm loginForm);

    CompletableFuture<String> saveAvatarImage(byte[] image, String fileName);

    DetailedUserInfo getCurrentUser();

    Optional<? extends UserInfo> availableLoginByPassword();

    CompletableFuture<? extends DetailedUserInfo> login(String password);

    void logout();
}