package org.banew.hdh.fxapp;

import java.io.IOException;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecondaryController {

    @Autowired
    private JavaFXApp javaFXApp;

    @FXML
    private void switchToPrimary() throws IOException {
        javaFXApp.changeScene("primary");
    }
}