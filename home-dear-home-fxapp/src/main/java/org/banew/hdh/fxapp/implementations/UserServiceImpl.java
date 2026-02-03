package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.core.api.users.forms.RegisterForm;
import org.banew.hdh.fxapp.implementations.xml.XmlUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorizationContext authorizationContext;

    @Override
    @Async
    public CompletableFuture<? extends User> register(RegisterForm registerForm) {

        XmlUser xmlUser = new XmlUser();
        xmlUser.setEmail(registerForm.email());
        xmlUser.setPassword(passwordEncoder.encode(registerForm.password()));
        xmlUser.setUsername(registerForm.username());
        storageRepository.saveUser(xmlUser);

        return CompletableFuture.completedFuture(xmlUser);
    }

    @Override
    @Async
    public CompletableFuture<? extends User> login(LoginForm loginForm) {
        var user = storageRepository
                .findByUsernameAndPassword(loginForm.username(), passwordEncoder.encode(loginForm.password()))
                .orElseThrow(() -> new IllegalArgumentException("Username or password is invalid"));

        return CompletableFuture.completedFuture(user);
    }

    @Override
    public User getCurrentUser() {
        return authorizationContext.getCurrentUser();
    }

    @Override
    public Optional<? extends User> availableLoginByPassword() {
        return storageRepository.findByLastTimeLoginAfterThan(LocalDateTime.now().minusDays(3));
    }

    @Override
    @Async
    public CompletableFuture<? extends User> login(String password) {
        User user = availableLoginByPassword()
                .orElseThrow(() -> new IllegalArgumentException("You can't login this way now!"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            authorizationContext.setCurrentUser(user);
            return CompletableFuture.completedFuture(user);
        }
        else {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Wrong password!"));
        }
    }

    @Override
    public void logout() {
        authorizationContext.logout();
    }
}
