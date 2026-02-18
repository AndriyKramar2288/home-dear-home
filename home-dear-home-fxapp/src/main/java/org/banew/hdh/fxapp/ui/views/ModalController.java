package org.banew.hdh.fxapp.ui.views;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ModalController {

    @Autowired
    private JavaFXApp javaFXApp;

    @FXML
    private Pane modalStackPane;
    @FXML
    private Pane modalWindow;
    @FXML
    private Button modalCloseButton;

    public void initialize() {
        setVisible(false);
        modalCloseButton.setOnAction(event -> {
            setVisible(false);
        });
    }

    public void setVisible(boolean visible) {
        modalWindow.setVisible(visible);
        if (javaFXApp.getScene() != null) {
            javaFXApp.getScene().getRoot().lookup("#mainContainer").setEffect(visible ? new GaussianBlur(): null);
        }
    }

    public void hideCloseButton() {
        modalCloseButton.setVisible(false);
    }

    public void setContent(Node content) {
        modalStackPane.getChildren().add(content);
        setVisible(true);
    }
}
