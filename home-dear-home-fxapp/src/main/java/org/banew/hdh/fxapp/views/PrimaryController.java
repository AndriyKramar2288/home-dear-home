package org.banew.hdh.fxapp.views;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.banew.hdh.fxapp.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrimaryController {

    @FXML
    private HBox topBar;

    @Autowired
    private JavaFXApp javaFXApp;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void handleTitleBarPressed(MouseEvent event) {
        if (event.getClickCount() == 2) {
            handleMaximize(new ActionEvent(event.getSource(), null));
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleMaximize(Event event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Stage stage = (Stage) scene.getWindow();

        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            JavaFXApp.resizeAsOpen(stage);
            stage.setMaximized(true);
        }

        scene.setFill(stage.isMaximized() ? Color.BLACK : Color.TRANSPARENT);
    }
}