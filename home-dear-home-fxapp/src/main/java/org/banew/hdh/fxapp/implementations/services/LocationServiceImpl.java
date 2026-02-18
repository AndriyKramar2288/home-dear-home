package org.banew.hdh.fxapp.implementations.services;

import lombok.AllArgsConstructor;
import org.banew.hdh.core.api.dto.ActionInfo;
import org.banew.hdh.core.api.dto.LocationInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.banew.hdh.fxapp.implementations.XmlStorageRepository;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.implementations.xml.XmlAction;
import org.banew.hdh.fxapp.implementations.xml.XmlLocation;
import org.banew.hdh.fxapp.implementations.xml.XmlLocationComponent;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService<ComponentsContext> {

    private final XmlStorageRepository xmlStorageRepository;
    private final ComponentsContext componentsContext;

    private final Map<String, String> locationIdToActionIdMapOfActiveActions = new HashMap<>();
    private final List<DesktopLocationComponent> activeComponents = new ArrayList<>();

    private final Set<String> enabledLocationsId = new HashSet<>();

    @Override
    public List<Class<? extends LocationComponent<ComponentsContext>>> getAvailableComponents() {
        return xmlStorageRepository.getAllAvailableComponents()
                .stream()
                .map(clazz -> (Class<? extends LocationComponent<ComponentsContext>>) clazz)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends LocationInfo> getLocations() {
        return xmlStorageRepository.getCurrentUserLocations().stream().toList();
    }

    @Override
    public Optional<? extends LocationInfo> getLocationById(String locationId) {
        return xmlStorageRepository.findLocationById(locationId);
    }

    @Override
    public LocationInfo createLocation(String name, String desc) {

        var location = new XmlLocation();
        location.setName(name);
        location.setDescription(desc);
        xmlStorageRepository.saveLocation(location);

        return location.copy();
    }

    @Override
    public LocationInfo updateLocation(String locationId, LocationInfo updatedLocationInfo) {
        var location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        location.setName(updatedLocationInfo.name());
        location.setDescription(updatedLocationInfo.description());
        xmlStorageRepository.saveLocation(location);
        return location.copy();
    }

    @Override
    public void removeLocation(String locationId) {
        xmlStorageRepository.deleteLocationById(locationId);
    }

    @Override
    public void reloadLocation(String locationId) {
        setEnabledLocation(locationId, false);
        setEnabledLocation(locationId, true);
    }

    @Override
    public void setEnabledLocation(String locationId, boolean enabled) {

        LocationInfo location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        if (enabled) {
            enabledLocationsId.add(locationId);
            enableLocation(location);
        } else {
            enabledLocationsId.remove(locationId);
            disableLocation(location);
        }
    }

    @Override
    public boolean isEnabledLocation(String locationId) {
        return enabledLocationsId.contains(locationId);
    }

    @Override
    public void createComponent(String locationId,
                                String name,
                                Class<? extends LocationComponent<ComponentsContext>> clazz,
                                Map<String, String> properties) {

        XmlLocation location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));


        String id = UUID.randomUUID().toString();
        var component = new XmlLocationComponent(id, name, properties, clazz.getPackageName());

        location.getComponents().add(component);
        xmlStorageRepository.saveLocation(location);
    }

    @Override
    public void updateAction(String locationId, String actionId, ActionInfo updatedAction) {
        var location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().stream()
                .filter(action -> actionId.equals(action.id()))
                .findFirst().ifPresent(action -> {
                    action.setSourceArgs(updatedAction.sourceArgs());
                    action.setTargetArgs(updatedAction.targetArgs());
                    action.setSourceComponentName(updatedAction.sourceComponentName());
                    action.setTargetComponentName(updatedAction.targetComponentName());
                });

        xmlStorageRepository.saveLocation(location);
    }

    @Override
    public void removeAction(String locationId, String actionId) {
        var location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().removeIf(action -> action.id().equals(actionId));
        xmlStorageRepository.saveLocation(location);
    }

    @Override
    public ActionInfo addAction(String locationId, ActionInfo action) {

        var savedAction = new XmlAction(UUID.randomUUID().toString(),
                action.sourceComponentName(), action.sourceArgs(), action.targetComponentName(), action.targetArgs());

        var location = xmlStorageRepository.findLocationById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().add(savedAction);
        xmlStorageRepository.saveLocation(location);

        return savedAction.copy();
    }

    private void enableLocation(LocationInfo location) {

        location.components().stream()
                .map(info -> {
                    try {
                        return (DesktopLocationComponent) Class.forName(info.fullClassName())
                                .getConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(activeComponents::add);

        for (DesktopLocationComponent component : activeComponents) {
            component.init(componentsContext);
        }

        if (location.actions() != null) for (ActionInfo action : location.actions()) {
            var source = activeComponents.stream()
                    .filter(c -> c.getInfo().id().equals(action.sourceComponentName()))
                    .findFirst().orElse(null);

            var target = activeComponents.stream()
                    .filter(c -> c.getInfo().id().equals(action.targetComponentName()))
                    .findFirst().orElse(null);

            if (source != null && target != null) {
                source.whenGenerate(data -> {
                            target.process(data, action.targetArgs());
                        }, action.sourceArgs())
                        .ifPresent(actionId -> locationIdToActionIdMapOfActiveActions.put(location.id(), actionId));
            }
        }
    }

    private void disableLocation(LocationInfo location) {
        locationIdToActionIdMapOfActiveActions.entrySet().removeIf(entry -> {
            if (location.id().equals(entry.getKey())) {
                activeComponents.forEach(component -> {
                    component.removeCallback(entry.getValue());
                });
            }
            return true;
        });
        activeComponents.clear();
    }
}
