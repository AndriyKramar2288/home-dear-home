package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {

    UserEntity create();

    Optional<? extends UserEntity> findByUsername(String username);

    Optional<? extends UserEntity> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin);

    void save(UserEntity xmlUser);
}
