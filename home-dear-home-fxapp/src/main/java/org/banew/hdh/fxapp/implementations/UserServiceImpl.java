package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.UserService;
import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.core.api.users.forms.RegisterForm;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public CompletableFuture<User> register(RegisterForm registerForm) {
//        var user = userRepository.findByUsernameAndPassword(login, password)
//                .orElseThrow(IllegalArgumentException::new);
//
//        return CompletableFuture.completedFuture(user);
        return null;
    }

    @Override
    public CompletableFuture<User> login(LoginForm loginForm) {
        return null;
    }

    @Override
    public User getCurrentUser() {
        return null;
    }
}
