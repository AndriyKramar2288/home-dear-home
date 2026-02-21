package org.banew.hdh.core.api.layers.data;

import org.banew.hdh.core.api.layers.data.entities.ActionEntity;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LocationRepository {

    LocationEntity create();

    ActionEntity createAction(String sourceComponentName,
                              Map<String, String> sourceArgs,
                              String targetComponentName,
                              Map<String, String> targetArgs);

    void save(LocationEntity location);

    List<? extends LocationEntity> findByUser(UserEntity user);

    Optional<? extends LocationEntity> findById(String locationId);

    void deleteById(String locationId);

    ComponentEntity createComponent(String name,
                                    Map<String, String> properties,
                                    String packageName,
                                    LocationComponentAttributes annotation);
}
