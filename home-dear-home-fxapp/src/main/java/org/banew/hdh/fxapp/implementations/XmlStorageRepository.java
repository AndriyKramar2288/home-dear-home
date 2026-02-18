package org.banew.hdh.fxapp.implementations;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import org.banew.hdh.core.api.dto.DataPrototype;
import org.banew.hdh.core.api.dto.LocationInfo;
import org.banew.hdh.fxapp.ReflectionsUtils;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.implementations.xml.XmlLocation;
import org.banew.hdh.fxapp.implementations.xml.XmlStorage;
import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XmlStorageRepository {
    @Autowired
    private XmlService xmlService;
    @Autowired
    private AuthorizationContext authorizationContext;

    private final File file = new File(XmlService.USER_FILES_PREFIX + "storage.xml");
    private XmlStorage xmlStorage;

    @Getter
    private Set<Class<? extends DesktopLocationComponent>> allAvailableComponents;

    @PostConstruct
    private void initial() {
        xmlStorage = xmlService.loadFromXml(file).orElse(new XmlStorage());
        allAvailableComponents = ReflectionsUtils.getAllImplementations(DesktopLocationComponent.class);
    }

    @PreDestroy
    private void saveData() throws JAXBException {
        xmlService.generateSchema();
        xmlService.saveToXml(xmlStorage, file);
    }

    public Optional<XmlUserInfo> findByUsername(String username) {
        return xmlStorage.getUsers().stream()
                .filter(u -> u.username().equals(username))
                .findFirst();
    }

    public Optional<XmlUserInfo> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin) {
        return xmlStorage.getUsers().stream()
                .filter(u -> u.lastTimeLogin().isAfter(lastTimeLogin))
                .min(Comparator.comparing(XmlUserInfo::lastTimeLogin));
    }

    public void saveUser(XmlUserInfo xmlUser) {
        xmlStorage.getUsers().add(xmlUser);
    }

    public void saveLocation(XmlLocation location) {

        if (location.id() == null) {
            location.setId(UUID.randomUUID().toString());
        }

        authorizationContext.getCurrentUser().getLocations().removeIf(l -> l.id().equals(location.id()));
        authorizationContext.getCurrentUser().getLocations().add(location);
    }

    public List<XmlLocation> getCurrentUserLocations() {
        return authorizationContext.getCurrentUser().getLocations().stream()
                .map(XmlLocation::copy)
                .collect(Collectors.toList());
    }

    public Optional<XmlLocation> findLocationById(String locationId) {
        return authorizationContext.getCurrentUser().getLocations().stream()
                .filter(l -> l.id().equals(locationId))
                .findFirst()
                .map(XmlLocation::copy);
    }

    public void deleteLocationById(String locationId) {
        authorizationContext.getCurrentUser()
                .setLocations(authorizationContext.getCurrentUser().getLocations().stream()
                        .filter(location -> !location.id().equals(locationId))
                        .collect(Collectors.toList()));
    }
}
