package org.banew.hdh.core.api.layers.components;

import org.banew.hdh.core.api.layers.data.entities.UserEntity;

public interface AuthorizationContext {
    UserEntity getCurrentUser();
    String getCurrentUserId();
    void setCurrentUserId(String currentUserId);
}
