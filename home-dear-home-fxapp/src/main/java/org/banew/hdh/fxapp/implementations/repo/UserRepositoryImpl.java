package org.banew.hdh.fxapp.implementations.repo;

import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.BasicMapper;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final XmlDataContainer data;
    private final AuthorizationContext authorizationContext;
    private final BasicMapper basicMapper;

    @Override
    public UserEntity create() {
        return new XmlUserInfo();
    }

    @Override
    public Optional<? extends UserEntity> findByUsername(String username) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<? extends UserEntity> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin) {
        return data.getXmlStorage().getUsers().stream()
                .filter(u -> u.getLastTimeLogin().isAfter(lastTimeLogin))
                .map(u -> (XmlUserInfo) u)
                .min(Comparator.comparing(XmlUserInfo::getLastTimeLogin));
    }

    @Override
    public void save(UserEntity user) {
        data.getXmlStorage().getUsers().add(user);
    }
}

