package org.banew.hdh.fxapp.implementations.repo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.ComponentsClassesManager;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.ReflectionsUtils;
import org.banew.hdh.fxapp.implementations.XmlService;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.implementations.xml.XmlStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class XmlDataContainer implements ComponentsClassesManager {
    @Autowired
    private XmlService xmlService;
    @Autowired
    private AuthorizationContext authorizationContext;

    private final File file = new File(XmlService.USER_FILES_PREFIX + "storage.xml");

    @Getter
    private XmlStorage xmlStorage;
    private Map<String, Class<? extends DesktopLocationComponent>> allAvailableComponents;

    @PostConstruct
    private void initial() {
        xmlStorage = xmlService.loadFromXml(file).orElse(new XmlStorage());
        allAvailableComponents = ReflectionsUtils.getAllImplementations(DesktopLocationComponent.class).stream()
                .collect(Collectors.toMap(Class::getName, e -> e));

        xmlStorage.getUsers().stream()
                .flatMap(u -> u.getLocations().stream())
                .flatMap(l -> l.getComponents().stream())
                .forEach(component -> {
                    var clazz = allAvailableComponents.get(component.getFullClassName());
                    if (clazz != null) {
                        component.setClassAttributes(clazz.getAnnotation(LocationComponentAttributes.class));
                    }
                });
    }

    @PreDestroy
    private void saveData() throws JAXBException {
        xmlService.generateSchema();
        xmlService.saveToXml(xmlStorage, file);
    }

    @Override
    public Set<LocationComponentAttributes> getAvailableComponents() {
        return allAvailableComponents.values().stream()
                .map(cl -> cl.getAnnotation(LocationComponentAttributes.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Class<? extends LocationComponent<?>> getLocationComponentClass(String classFullname) {
        return allAvailableComponents.get(classFullname);
    }
}
