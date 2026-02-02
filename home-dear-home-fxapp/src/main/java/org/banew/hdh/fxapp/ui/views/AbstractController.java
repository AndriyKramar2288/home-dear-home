package org.banew.hdh.fxapp.ui.views;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    @Autowired
    protected JavaFXApp javaFXApp;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void handleTitleBarPressed(MouseEvent event) {
        if (event.getClickCount() == 2) {
            javaFXApp.maximize();
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!stage.isMaximized()) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }
    }

    @FXML
    private void handleTitleBarDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!stage.isMaximized()) {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        }
    }

    @FXML
    public void closeHandle() {
        Platform.exit();
    }

    @FXML
    private void handleMinimize(Event event) {
        javaFXApp.minimize();
    }

    @FXML
    private void handleMaximize(Event event) {
        javaFXApp.maximize();
    }
}
