package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {

    void save(LocationEntity location);

    List<LocationEntity> findByUser(UserEntity user);

    Optional<LocationEntity> findById(String locationId);

    void deleteById(String locationId);
}
