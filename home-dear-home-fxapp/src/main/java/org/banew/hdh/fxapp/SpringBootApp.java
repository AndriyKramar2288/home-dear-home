package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.banew.hdh.fxapp.implementations.xml.*;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringBootApp {
    @Bean
    public JavaFXApp javaFXApp() {
        return JavaFXApp.getInstance();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JAXBContext jaxbContext() {
        try {
            //return JAXBContext.newInstance(XmlAction.class, XmlLocation.class, XmlLocationComponent.class, XmlStorage.class, XmlUserInfo.class);
            return ReflectionsUtils.createDynamicContext("org.banew.hdh.fxapp.implementations.xml");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}