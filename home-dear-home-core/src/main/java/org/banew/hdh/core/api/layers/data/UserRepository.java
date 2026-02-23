package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin);

    void save(UserEntity user);

    Optional<UserEntity> findById(String id);
}
