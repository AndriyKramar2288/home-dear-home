package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin);

    void save(UserEntity user);

    Optional<UserEntity> findById(String id);
}
