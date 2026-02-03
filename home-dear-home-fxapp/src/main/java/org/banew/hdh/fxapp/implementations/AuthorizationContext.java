package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.users.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthorizationContext {
    private final AtomicReference<User> currentUser = new AtomicReference<>();

    public void logout() {
        currentUser.set(null);
    }

    public User getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
}
