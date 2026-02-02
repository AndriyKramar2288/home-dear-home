package org.banew.hdh.fxapp.implementations;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.bind.JAXBException;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.users.User;
import org.banew.hdh.fxapp.implementations.xml.XmlLocation;
import org.banew.hdh.fxapp.implementations.xml.XmlStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public class StorageRepository {
    @Autowired
    private XmlService xmlService;

    private final File file = new File("storage.xml");
    private XmlStorage xmlStorage;

    public Optional<? extends User> findByUsernameAndPassword(String username, String password) {
        return xmlStorage.getUsers().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    public void deleteMe(Pane pane) {
        xmlStorage.getUsers().forEach(user -> user.getLocations().forEach(location -> {
            ((XmlLocation) location).init(pane);
        }));
    }

    @PostConstruct
    private void initial() {
        xmlStorage = xmlService.loadFromXml(file).orElse(new XmlStorage());
    }

    @PreDestroy
    private void saveData() throws JAXBException {
        xmlService.generateSchema();
        xmlService.saveToXml(xmlStorage, file);
    }
}
