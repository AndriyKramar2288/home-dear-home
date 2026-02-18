package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.dto.ActionInfo;
import org.banew.hdh.core.api.dto.LocationInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LocationService<T> {

    List<Class<? extends LocationComponent<T>>> getAvailableComponents();

    List<? extends LocationInfo> getLocations();

    Optional<? extends LocationInfo> getLocationById(String locationId);

    LocationInfo createLocation(String name, String desc);

    LocationInfo updateLocation(String locationId, LocationInfo updatedLocationInfo);

    void removeLocation(String locationId);

    void reloadLocation(String locationId);

    void setEnabledLocation(String locationId, boolean enabled);

    boolean isEnabledLocation(String locationId);

    void createComponent(String locationId,
                         String name,
                         Class<? extends LocationComponent<T>> clazz,
                         Map<String, String> properties);

    ActionInfo addAction(String locationId, ActionInfo action);

    void updateAction(String locationId, String actionId, ActionInfo updatedAction);

    void removeAction(String locationId, String actionId);
}