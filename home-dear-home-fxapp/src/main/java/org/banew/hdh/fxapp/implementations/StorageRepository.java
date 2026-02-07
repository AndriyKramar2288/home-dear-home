package org.banew.hdh.fxapp.implementations;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import org.banew.hdh.core.api.domen.LocationInfo;
import org.banew.hdh.core.api.domen.UserInfo;
import org.banew.hdh.fxapp.ReflectionsUtils;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.implementations.xml.XmlLocation;
import org.banew.hdh.fxapp.implementations.xml.XmlStorage;
import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StorageRepository {
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

    public Optional<? extends UserInfo> findByUsername(String username) {
        return xmlStorage.getUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public Optional<? extends UserInfo> findByLastTimeLoginAfterThan(LocalDateTime lastTimeLogin) {
        return xmlStorage.getUsers().stream()
                .filter(u -> u.getLastTimeLogin().isAfter(lastTimeLogin))
                .min(Comparator.comparing(XmlUserInfo::getLastTimeLogin));
    }

    public void saveUser(XmlUserInfo xmlUser) {
        xmlStorage.getUsers().add(xmlUser);
    }

    public void createLocationByName(String name) {
        LocationInfo location = new XmlLocation();
        location.setName(name);
        authorizationContext.getCurrentUser().getLocations().add(location);
    }

    public List<LocationInfo> getCurrentUserLocations() {
        return authorizationContext.getCurrentUser().getLocations().stream().toList();
    }

    public Optional<LocationInfo> findLocationById(String locationId) {
        return authorizationContext.getCurrentUser().getLocations().stream()
                .filter(l -> l.getId().equals(locationId))
                .findFirst();
    }

    public void deleteLocationById(String locationId) {
        authorizationContext.getCurrentUser().getLocations().removeIf(l -> l.getId().equals(locationId));
    }
}
