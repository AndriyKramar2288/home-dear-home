package org.banew.hdh.core.api.services;

import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Action;

import java.util.List;
import java.util.Map;

public interface LocationService {

    List<Class<? extends LocationComponent>> getAvailableComponents();
    List<Location> getLocations();

    void createLocation(String name);
    void removeLocation(String locationId);

    void setEnabledLocation(String locationId, boolean enabled);
    boolean isEnabledLocation(String locationId);

    void createComponent(String locationId, Class<? extends LocationComponent> clazz, Map<String, String> properties);
    void createAction(String locationId, Action action);
}
