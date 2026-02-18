package org.banew.hdh.fxapp.ui.views;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
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
            animateBlur(javaFXApp.getScene().getRoot().lookup("#mainContainer"), visible);
        }
    }

    private void animateBlur(Node node, boolean visible) {
        GaussianBlur blur = (node.getEffect() instanceof GaussianBlur g)
                ? g
                : new GaussianBlur(0);
        if (node.getEffect() == null) node.setEffect(blur);
        double targetRadius = visible ? 15.0 : 0.0;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(100),
                        new KeyValue(blur.radiusProperty(), targetRadius, Interpolator.EASE_BOTH)
                )
        );
        if (!visible) {
            timeline.setOnFinished(e -> node.setEffect(null));
        }
        timeline.play();
    }

    public void setContent(Node content, boolean isCloseButtonVisible) {
        modalCloseButton.setVisible(isCloseButtonVisible);
        modalStackPane.getChildren().clear();
        modalStackPane.getChildren().add(content);
        setVisible(true);
    }
}
