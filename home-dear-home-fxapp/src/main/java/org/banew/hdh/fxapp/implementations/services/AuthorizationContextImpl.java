package org.banew.hdh.fxapp.implementations.services;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthorizationContextImpl implements AuthorizationContext {

    private final AtomicReference<String> currentUserIdRef = new AtomicReference<>();
    private final UserRepository userRepository;

    @Override
    public UserEntity getCurrentUser() {
        return userRepository.findById(currentUserIdRef.get()).orElse(null);
    }

    @Override
    public String getCurrentUserId() {
        return currentUserIdRef.get();
    }

    @Override
    public void setCurrentUserId(String currentUserId) {
        currentUserIdRef.set(currentUserId);
    }
}
