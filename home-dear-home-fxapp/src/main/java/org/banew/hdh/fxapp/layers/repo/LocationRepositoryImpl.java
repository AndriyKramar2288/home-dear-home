package org.banew.hdh.fxapp.layers.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.data.LocationRepository;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.layers.services.XmlEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class LocationRepositoryImpl implements LocationRepository {

    private final XmlDataContainer xmlDataContainer;
    private final XmlEntityMapper xmlEntityMapper;
    private final XmlDataContainer data;

    @Override
    public void save(LocationEntity location) {

        if (location.getId() == null) {
            location.setId(UUID.randomUUID().toString());
        }

        AtomicBoolean changed = new AtomicBoolean(false);

        var actualUser = xmlDataContainer.getXmlStorage().getUsers().stream()
                .filter(u -> u.getId().equals(location.getOwner().getId()))
                .findFirst().orElseThrow();

        actualUser.setLocations(actualUser.getLocations().stream()
                .map(l -> {
                    if (l.getId().equals(location.getId())) {
                        changed.set(true);
                        return xmlEntityMapper.locationEntityToXml(location);
                    }
                    return l;
                }).collect(Collectors.toList()));

        if (!changed.get()) {
            actualUser.getLocations().add(xmlEntityMapper.locationEntityToXml(location));
        }
    }

    @Override
    public List<LocationEntity> findByUser(UserEntity user) {
        return user == null ? List.of() : List.copyOf(user.getLocations());
    }

    @Override
    public Optional<LocationEntity> findById(String locationId) {
        return data.getXmlStorage().getUsers().stream()
                .flatMap(u -> u.getLocations().stream())
                .filter(l -> l.getId().equals(locationId))
                .map(xmlEntityMapper::locationXmlToEntity)
                .findFirst();
    }

    @Override
    public void deleteById(String locationId) {
        data.getXmlStorage().getUsers().forEach(
                u -> u.setLocations(u.getLocations().stream()
                        .filter(l ->
                                !l.getId().equals(locationId)).toList()));
    }
}
