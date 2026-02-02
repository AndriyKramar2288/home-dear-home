package org.banew.hdh.fxapp;

import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Entrypoint {
    private static final Logger log = LoggerFactory.getLogger(Entrypoint.class);

    public static void main(String[] args) {
        log.info("Starting Entrypoint");
        JavaFXApp.launch(JavaFXApp.class);
    }
}