package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.fxapp.implementations.xml.*;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            return ReflectionsUtils.createDynamicContext("org.banew.hdh.fxapp.implementations.xml");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}