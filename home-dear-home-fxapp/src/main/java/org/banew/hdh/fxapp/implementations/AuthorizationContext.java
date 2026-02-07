package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.domen.UserInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthorizationContext {
    private final AtomicReference<UserInfo> currentUser = new AtomicReference<>();

    public void logout() {
        currentUser.set(null);
    }

    public UserInfo getCurrentUser() {
        return currentUser.get();
    }

    public void setCurrentUser(UserInfo userInfo) {
        currentUser.set(userInfo);
    }
}
