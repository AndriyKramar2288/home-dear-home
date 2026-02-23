package org.banew.hdh.fxapp.layers.repo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.components.ComponentsClassesManager;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.utils.ReflectionsUtils;
import org.banew.hdh.fxapp.layers.services.XmlService;
import org.banew.hdh.fxapp.implementations.runtime.DesktopLocationComponent;
import org.banew.hdh.fxapp.layers.repo.xml.XmlStorage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class XmlDataContainerImpl implements ComponentsClassesManager, XmlDataContainer {

    private final XmlService xmlService;

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
    public Set<AvailableComponent> getAvailableComponents() {
        return allAvailableComponents.values().stream()
                .map(cl -> new AvailableComponent(cl.getName(), cl.getAnnotation(LocationComponentAttributes.class)))
                .collect(Collectors.toSet());
    }

    @Override
    public Class<? extends LocationComponent<?>> getLocationComponentClass(String classFullname) {
        return allAvailableComponents.get(classFullname);
    }
}
