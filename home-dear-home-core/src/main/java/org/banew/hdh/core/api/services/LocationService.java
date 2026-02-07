package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.domen.ActionInfo;
import org.banew.hdh.core.api.domen.LocationInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;

import java.util.List;
import java.util.Map;

public interface LocationService<T> {

    List<Class<? extends LocationComponent<T>>> getAvailableComponents();
    List<LocationInfo> getLocations();

    void createLocation(String name);
    void removeLocation(String locationId);

    void setEnabledLocation(String locationId, boolean enabled);
    boolean isEnabledLocation(String locationId);

    void createComponent(String locationId,
                         String name,
                         Class<? extends LocationComponent<T>> clazz,
                         Map<String, String> properties);

    void createAction(String locationId, ActionInfo action);
}