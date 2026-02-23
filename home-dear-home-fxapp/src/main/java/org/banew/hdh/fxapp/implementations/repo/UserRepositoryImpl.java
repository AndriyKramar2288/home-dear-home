package org.banew.hdh.fxapp.implementations.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.fxapp.implementations.services.XmlEntityMapper;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final XmlDataContainer data;
    private final XmlEntityMapper mapper;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .map(mapper::userXmlToEntity)
                .findFirst();
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

