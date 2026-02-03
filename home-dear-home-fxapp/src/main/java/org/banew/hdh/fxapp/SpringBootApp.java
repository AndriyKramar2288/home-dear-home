package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.banew.hdh.fxapp.implementations.xml.*;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootApp {
    @Bean
    public JavaFXApp javaFXApp() {
        return JavaFXApp.getInstance();
    }

    @Bean
    public JAXBContext jaxbContext() {
        try {
            return JAXBContext.newInstance(XmlStorage.class,
                    XmlUser.class,
                    XmlLocation.class,
                    ButtonComponent.class,
                    XmlLocationComponent.class,
                    CounterComponent.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}