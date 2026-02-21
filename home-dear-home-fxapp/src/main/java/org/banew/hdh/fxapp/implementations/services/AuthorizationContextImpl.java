package org.banew.hdh.fxapp.implementations.services;

import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class AuthorizationContextImpl implements AuthorizationContext {
    private final AtomicReference<UserEntity> currentUser = new AtomicReference<>();

    @Override
    public UserEntity getCurrentUser() {
        return currentUser.get();
    }

    @Override
    public void setCurrentUser(UserEntity userInfo) {
        currentUser.set(userInfo);
    }
}
