package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.BasicMapper;
import org.banew.hdh.core.api.layers.components.ComponentsClassesManager;
import org.banew.hdh.core.api.layers.components.ComponentsContextSource;
import org.banew.hdh.core.api.layers.data.LocationRepository;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.implementations.LocationServiceImpl;
import org.banew.hdh.core.implementations.UserServiceImpl;
import org.banew.hdh.fxapp.implementations.xml.*;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public BasicMapper basicMapper() {
        return BasicMapper.INSTANCE;
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Bean
    public UserServiceImpl userService(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder,
                                       BasicMapper basicMapper,
                                       AuthorizationContext authorizationContext) {
        return new UserServiceImpl(userRepository, passwordEncoder, authorizationContext, basicMapper);
    }

    @Bean
    public LocationServiceImpl locationService(LocationRepository locationRepository,
                                               ComponentsClassesManager componentsClassesManager,
                                               BasicMapper basicMapper,
                                               AuthorizationContext authorizationContext,
                                               ComponentsContextSource<?> componentsContextSource) {
        return new LocationServiceImpl(locationRepository,
                componentsClassesManager,
                authorizationContext,
                basicMapper,
                componentsContextSource);
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