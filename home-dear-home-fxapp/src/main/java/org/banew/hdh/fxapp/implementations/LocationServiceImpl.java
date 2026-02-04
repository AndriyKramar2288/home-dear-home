package org.banew.hdh.fxapp.implementations;

import lombok.AllArgsConstructor;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Action;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.fxapp.implementations.xml.AbstractWidgetLocationComponent;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final StorageRepository storageRepository;

    private final Map<String, String> locationIdToActionIdMap = new HashMap<>();

    @Override
    public List<Class<? extends LocationComponent>> getAvailableComponents() {
        return storageRepository.getAllAvailableComponents()
                .stream()
                .toList();
    }

    @Override
    public List<Location> getLocations() {
        return storageRepository.getCurrentUserLocations();
    }

    @Override
    public void createLocation(String name) {
        storageRepository.createLocationByName(name);
    }

    @Override
    public void removeLocation(String locationId) {
        storageRepository.deleteLocationById(locationId);
    }

    @Override
    public void setEnabledLocation(String locationId, boolean enabled) {

        Location location = storageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        if (enabled) {
            enableLocation(location);
        } else {
            disableLocation(location);
        }
    }

    @Override
    public boolean isEnabledLocation(String locationId) {
        return false;
    }

    @Override
    public void createComponent(String locationId,
                                Class<? extends LocationComponent> clazz,
                                Map<String, String> properties) {

        Location location = storageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        try {
            LocationComponent component = clazz.getDeclaredConstructor().newInstance();
            properties.forEach(component::setProperty);

            location.getComponents().add(component);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAction(String locationId, Action action) {
        storageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"))
                .getActions().add(action);
    }

    private void enableLocation(Location location) {
        List<? extends LocationComponent> components = location.getComponents();
        List<? extends Action> actions = location.getActions();

        for (LocationComponent component : components) {
            if (component instanceof AbstractWidgetLocationComponent<?> widgetComponent) {
                widgetComponent.init(null);
            }
        }

        if (actions != null) for (Action action : actions) {
            var source = components.stream()
                    .filter(c -> c.getName().equals(action.sourceComponentName()))
                    .findFirst().orElse(null);

            var target = components.stream()
                    .filter(c -> c.getName().equals(action.targetComponentName()))
                    .findFirst().orElse(null);

            if (source != null && target != null) {
                source.whenGenerate(data -> {
                            data.loadInto(target, action.targetArgs());
                        }, action.sourceArgs())
                        .ifPresent(actionId -> locationIdToActionIdMap.put(location.getId(), actionId));
            }
        }
    }

    private void disableLocation(Location location) {
        locationIdToActionIdMap.entrySet().removeIf(entry -> {
            if (location.getId().equals(entry.getKey())) {
                location.getComponents().forEach(component -> {
                    component.removeCallback(entry.getValue());
                });
            }
            return true;
        });
    }
}
