package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;

import java.util.Optional;

public interface ComponentRepository {
    Optional<ComponentEntity> findById(String sel);
}