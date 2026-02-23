package org.banew.hdh.core.implementations;

import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.BasicMapper;
import org.banew.hdh.core.api.layers.components.ComponentsClassesManager;
import org.banew.hdh.core.api.layers.components.ComponentsContextSource;
import org.banew.hdh.core.api.layers.data.ComponentRepository;
import org.banew.hdh.core.api.layers.data.LocationRepository;
import org.banew.hdh.core.api.layers.data.entities.ActionEntity;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.services.LocationService;
import org.banew.hdh.core.api.layers.services.dto.ActionDto;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final ComponentRepository componentRepository;
    private final ComponentsClassesManager classesManager;
    private final AuthorizationContext authorizationContext;
    private final BasicMapper basicMapper;
    private final ComponentsContextSource<?> componentsContextSource;

    private final Map<String, String> locationIdToActionIdMapOfActiveActions = new ConcurrentHashMap<>();
    private final List<LocationComponent<?>> activeComponents = new CopyOnWriteArrayList<>();

    private final Set<String> enabledLocationsId = new HashSet<>();

    public <T> LocationServiceImpl(LocationRepository locationRepository, ComponentRepository componentRepository,
                                   ComponentsClassesManager classesManager,
                                   AuthorizationContext authorizationContext,
                                   BasicMapper basicMapper,
                                   ComponentsContextSource<T> componentsContextSource) {
        this.locationRepository = locationRepository;
        this.componentRepository = componentRepository;
        this.classesManager = classesManager;
        this.authorizationContext = authorizationContext;
        this.basicMapper = basicMapper;
        this.componentsContextSource = componentsContextSource;
    }

    @SuppressWarnings("unchecked")
    private <T> void initializeComponent(LocationComponent<T> component) {
        // Тепер Java бачить, що component чекає тип T
        // Нам треба привести наш сорс до цього ж типу T
        // Це "безпечне" приведення, бо ми самі контролюємо логіку контексту
        T context = (T) componentsContextSource.get();
        component.init(context);
    }

    @Override
    public List<AvailableComponent> getAvailableComponents() {
        return classesManager.getAvailableComponents().stream().toList();
    }

    @Override
    public List<LocationDto> getLocations() {
        return locationRepository.findByUser(authorizationContext.getCurrentUser()).stream()
                .map(basicMapper::locationEntityToDto)
                .toList();
    }

    @Override
    public Optional<LocationDto> getLocationById(String locationId) {
        return locationRepository.findById(locationId).map(basicMapper::locationEntityToDto);
    }

    @Override
    public Optional<LocationComponentDto> getLocationComponentById(String locationId) {
        return componentRepository.findById(locationId).map(basicMapper::componentEntityToDto);
    }

    @Override
    public LocationDto createLocation(String name, String desc) {

        var location = new LocationEntity();
        location.setName(name);
        location.setDescription(desc);
        location.setOwnerId(authorizationContext.getCurrentUser().getId());
        locationRepository.save(location);

        return basicMapper.locationEntityToDto(location);
    }

    @Override
    public LocationDto updateLocation(String locationId, LocationDto updatedLocationDto) {
        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        location.setName(updatedLocationDto.name());
        location.setDescription(updatedLocationDto.description());
        location.getComponents().forEach(component -> {
            updatedLocationDto.components().stream()
                    .filter(c -> c.id().equals(component.getId()))
                    .findFirst()
                    .ifPresent(newLocationComponent -> {
                        component.setName(newLocationComponent.name());
                        component.setProperties(new HashMap<>(newLocationComponent.properties()));
                    });
        });
        locationRepository.save(location);
        return basicMapper.locationEntityToDto(location);
    }

    @Override
    public void removeLocation(String locationId) {
        locationRepository.deleteById(locationId);
    }

    @Override
    public void reloadLocation(String locationId) {
        setEnabledLocation(locationId, false);
        setEnabledLocation(locationId, true);
    }

    @Override
    public void setEnabledLocation(String locationId, boolean enabled) {

        LocationEntity location = locationRepository.findById(locationId)
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
    public void createComponent(String locationId, String name, String classFullname, Map<String, String> properties) {
        LocationEntity location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        String id = UUID.randomUUID().toString();
        var clazz = classesManager.getLocationComponentClass(classFullname);

        var component = new ComponentEntity();
        component.setName(name);
        component.setProperties(properties);
        component.setFullClassName(classFullname);
        component.setClassAttributes(clazz.getAnnotation(LocationComponentAttributes.class));

        location.getComponents().add(component);
        locationRepository.save(location);
    }

    @Override
    public void updateAction(String locationId, String actionId, ActionDto updatedAction) {
        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().stream()
                .filter(action -> actionId.equals(action.getActionId()))
                .findFirst().ifPresent(action -> {
                    action.setSourceArgs(updatedAction.sourceArgs());
                    action.setTargetArgs(updatedAction.targetArgs());
                    action.setSourceComponentName(updatedAction.sourceComponentName());
                    action.setTargetComponentName(updatedAction.targetComponentName());
                });

        locationRepository.save(location);
    }

    @Override
    public void removeAction(String locationId, String actionId) {
        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().removeIf(action -> action.getActionId().equals(actionId));
        locationRepository.save(location);
    }

    @Override
    public ActionDto addAction(String locationId, ActionDto action) {

        var savedAction = new ActionEntity();
        savedAction.setSourceArgs(action.sourceArgs());
        savedAction.setTargetArgs(action.targetArgs());
        savedAction.setSourceComponentName(action.sourceComponentName());
        savedAction.setTargetComponentName(action.targetComponentName());

        var location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found!"));

        location.getActions().add(savedAction);
        locationRepository.save(location);

        return basicMapper.actionEntityToDto(savedAction);
    }

    private void enableLocation(LocationEntity location) {

        location.getComponents().stream()
                .map(info -> {
                    try {
                        return (LocationComponent<?>) Class.forName(info.getFullClassName())
                                .getConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(activeComponents::add);

        for (LocationComponent<?> component : activeComponents) {
            initializeComponent(component);
        }

        if (location.getActions() != null) for (ActionEntity action : location.getActions()) {
            var source = activeComponents.stream()
                    .filter(c -> c.getInfo().id().equals(action.getSourceComponentName()))
                    .findFirst().orElse(null);

            var target = activeComponents.stream()
                    .filter(c -> c.getInfo().id().equals(action.getTargetComponentName()))
                    .findFirst().orElse(null);

            if (source != null && target != null) {
                source.whenGenerate(data -> {
                            target.process(data, action.getTargetArgs());
                        }, action.getSourceArgs())
                        .ifPresent(actionId -> locationIdToActionIdMapOfActiveActions.put(location.getId(), actionId));
            }
        }
    }

    private void disableLocation(LocationEntity location) {
        locationIdToActionIdMapOfActiveActions.entrySet().removeIf(entry -> {
            if (location.getId().equals(entry.getKey())) {
                activeComponents.forEach(component -> {
                    component.removeCallback(entry.getValue());
                });
            }
            return true;
        });
        activeComponents.clear();
    }
}
