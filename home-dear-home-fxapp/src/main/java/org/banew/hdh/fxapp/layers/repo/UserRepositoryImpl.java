package org.banew.hdh.fxapp.layers.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.fxapp.layers.services.XmlEntityMapper;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {

    private final XmlDataContainer data;
    private final XmlEntityMapper mapper;

    @Override
    public List<UserEntity> findByUsername(String username) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .map(mapper::userXmlToEntity)
                .toList();
    }

    @Override
    public Optional<UserEntity> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getLastTimeLogin().isAfter(lastTimeLogin))
                .map(mapper::userXmlToEntity)
                .min(Comparator.comparing(UserEntity::getLastTimeLogin));
    }

    @Override
    public void save(UserEntity user) {

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }

        var currentUserOptional = data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst();

        AtomicBoolean isChanged = new AtomicBoolean(false);

        data.getXmlStorage().setUsers(data.getXmlStorage().getUsers().stream()
                .map(u -> {
                    if (u.getId().equals(user.getId())) {
                        isChanged.set(true);
                        return mapper.userEntityToXml(user);
                    } else {
                        return u;
                    }
                }).collect(Collectors.toList()));

        if (!isChanged.get()) {
            data.getXmlStorage().getUsers().add(mapper.userEntityToXml(user));
        }
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getId().equals(id))
                .map(mapper::userXmlToEntity)
                .findFirst();
    }
}

