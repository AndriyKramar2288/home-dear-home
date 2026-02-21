package org.banew.hdh.fxapp.implementations.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.data.LocationRepository;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.ActionEntity;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.implementations.xml.XmlAction;
import org.banew.hdh.fxapp.implementations.xml.XmlLocation;
import org.banew.hdh.fxapp.implementations.xml.XmlLocationComponent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final AuthorizationContext authorizationContext;
    private final XmlDataContainer data;

    @Override
    public LocationEntity create() {
        return new XmlLocation();
    }

    @Override
    public ActionEntity createAction(String sourceComponentName,
                                     Map<String, String> sourceArgs,
                                     String targetComponentName,
                                     Map<String, String> targetArgs) {
        return new XmlAction(targetComponentName, sourceComponentName, sourceArgs, targetComponentName, targetArgs);
    }

    @Override
    public void save(LocationEntity location) {

        if (location.getId() == null) {
            location.setId(UUID.randomUUID().toString());
        }

        authorizationContext.getCurrentUser().setLocations(
                authorizationContext.getCurrentUser().getLocations().stream()
                        .map(l -> l.getId().equals(location.getId()) ? location : l)
                        .toList());
    }

    @Override
    public List<? extends LocationEntity> findByUser(UserEntity user) {
        return user.getLocations();
    }

    @Override
    public Optional<? extends LocationEntity> findById(String locationId) {
        return data.getXmlStorage().getUsers().stream()
                .flatMap(u -> u.getLocations().stream())
                .filter(l -> l.getId().equals(locationId))
                .findFirst();
    }

    @Override
    public void deleteById(String locationId) {
        data.getXmlStorage().getUsers().forEach(
                u -> u.getLocations().removeIf(l -> l.getId().equals(locationId)));
    }

    @Override
    public ComponentEntity createComponent(String name, Map<String, String> properties, String packageName, LocationComponentAttributes annotation) {
        return new XmlLocationComponent(UUID.randomUUID().toString(), name, properties, packageName, annotation);
    }
}
