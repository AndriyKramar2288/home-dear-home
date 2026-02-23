package org.banew.hdh.fxapp;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.BasicMapper;
import org.banew.hdh.core.api.layers.components.ComponentsClassesManager;
import org.banew.hdh.core.api.layers.components.ComponentsContextSource;
import org.banew.hdh.core.api.layers.data.ComponentRepository;
import org.banew.hdh.core.api.layers.data.LocationRepository;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.implementations.LocationServiceImpl;
import org.banew.hdh.core.implementations.UserServiceImpl;
import org.banew.hdh.fxapp.layers.services.XmlEntityMapper;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.banew.hdh.fxapp.utils.ReflectionsUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
    public XmlEntityMapper xmlEntityMapper() {
        return XmlEntityMapper.INSTANCE;
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
                                               ComponentRepository componentRepository,
                                               AuthorizationContext authorizationContext,
                                               ComponentsContextSource<?> componentsContextSource) {
        return new LocationServiceImpl(locationRepository,
                componentRepository,
                componentsClassesManager,
                authorizationContext,
                basicMapper,
                componentsContextSource);
    }

    @Bean
    public JAXBContext jaxbContext() {
        try {
            return ReflectionsUtils.createDynamicContext("org.banew.hdh.fxapp.layers.repo.xml");
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}