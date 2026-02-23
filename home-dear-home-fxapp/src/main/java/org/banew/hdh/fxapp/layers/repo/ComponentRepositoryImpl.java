package org.banew.hdh.fxapp.layers.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.data.ComponentRepository;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.fxapp.layers.services.XmlEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class ComponentRepositoryImpl implements ComponentRepository {

    private final XmlDataContainer xmlDataContainer;
    private final XmlEntityMapper xmlEntityMapper;

    @Override
    public Optional<ComponentEntity> findById(String sel) {
        return xmlDataContainer.getXmlStorage().getUsers().stream()
                .flatMap(u -> u.getLocations().stream())
                .flatMap(l -> l.getComponents().stream())
                .filter(e -> e.getId().equals(sel))
                .map(xmlEntityMapper::componentXmlToEntity)
                .findFirst();
    }
}
