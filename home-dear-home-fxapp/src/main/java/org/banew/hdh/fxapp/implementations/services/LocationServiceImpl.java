package org.banew.hdh.fxapp.implementations.services;

import lombok.AllArgsConstructor;
import org.banew.hdh.core.api.domen.ActionInfo;
import org.banew.hdh.core.api.domen.LocationComponentInfo;
import org.banew.hdh.core.api.domen.LocationInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.banew.hdh.fxapp.implementations.StorageRepository;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.implementations.xml.XmlLocationComponent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService<ComponentsContext> {

    private final StorageRepository storageRepository;
    private final ComponentsContext componentsContext;

    private final Map<String, String> locationIdToActionIdMap = new HashMap<>();
    private final List<DesktopLocationComponent> components = new ArrayList<>();

    @Override
    public List<Class<? extends LocationComponent<ComponentsContext>>> getAvailableComponents() {
        return storageRepository.getAllAvailableComponents()
                .stream()
                .map(clazz -> (Class<? extends LocationComponent<ComponentsContext>>) clazz)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationInfo> getLocations() {
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

        LocationInfo location = storageRepository.findLocationById(locationId)
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
                                String name,
                                Class<? extends LocationComponent<ComponentsContext>> clazz,
                                Map<String, String> properties) {

        LocationInfo location = storageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));


        String id = UUID.randomUUID().toString();
        LocationComponentInfo component = new XmlLocationComponent(id, name, properties, clazz.getPackageName());

        location.getComponents().add(component);
    }

    @Override
    public void createAction(String locationId, ActionInfo action) {
        storageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"))
                .getActions().add(action);
    }

    private void enableLocation(LocationInfo location) {

        location.getComponents().stream()
                .map(info -> {
                    try {
                        return (DesktopLocationComponent) Class.forName(info.getFullClassName())
                                .getConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(components::add);

        for (DesktopLocationComponent component : components) {
            component.init(componentsContext);
        }

        if (location.getActions() != null) for (ActionInfo action : location.getActions()) {
            var source = components.stream()
                    .filter(c -> c.getInfo().getId().equals(action.sourceComponentName()))
                    .findFirst().orElse(null);

            var target = components.stream()
                    .filter(c -> c.getInfo().getId().equals(action.targetComponentName()))
                    .findFirst().orElse(null);

            if (source != null && target != null) {
                source.whenGenerate(data -> {
                            target.process(data, action.targetArgs());
                        }, action.sourceArgs())
                        .ifPresent(actionId -> locationIdToActionIdMap.put(location.getId(), actionId));
            }
        }
    }

    private void disableLocation(LocationInfo location) {
        locationIdToActionIdMap.entrySet().removeIf(entry -> {
            if (location.getId().equals(entry.getKey())) {
                components.forEach(component -> {
                    component.removeCallback(entry.getValue());
                });
            }
            return true;
        });
        components.clear();
    }
}
