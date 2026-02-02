package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.users.RegisterForm;
import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CompletableFuture<User> register(RegisterForm registerForm) {
        return null;
    }

    @Override
    public CompletableFuture<User> login(String login, String password) {

        var user = userRepository.findByUsernameAndPassword(login, password)
                .orElseThrow(IllegalArgumentException::new);

        return CompletableFuture.completedFuture(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return Optional.empty();
    }
}
