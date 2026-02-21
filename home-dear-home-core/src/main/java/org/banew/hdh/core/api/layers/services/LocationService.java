package org.banew.hdh.core.api.layers.services;

import org.banew.hdh.core.api.layers.services.dto.ActionDto;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LocationService {

    List<AvailableComponent> getAvailableComponents();

    List<? extends LocationDto> getLocations();

    Optional<? extends LocationDto> getLocationById(String locationId);

    LocationDto createLocation(String name, String desc);

    LocationDto updateLocation(String locationId, LocationDto updatedLocationDto);

    void removeLocation(String locationId);

    void reloadLocation(String locationId);

    void setEnabledLocation(String locationId, boolean enabled);

    boolean isEnabledLocation(String locationId);

    void createComponent(String locationId,
                         String name,
                         String classFullname,
                         Map<String, String> properties);

    ActionDto addAction(String locationId, ActionDto action);

    void updateAction(String locationId, String actionId, ActionDto updatedAction);

    void removeAction(String locationId, String actionId);
}