package org.banew.hdh.fxapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootApp {
    @Bean
    public JavaFXApp javaFXApp() {
        return JavaFXApp.getInstance();
    }
}