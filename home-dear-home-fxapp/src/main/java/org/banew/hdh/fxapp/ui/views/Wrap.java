package org.banew.hdh.fxapp.ui.views;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Wrap {

    @Autowired
    private JavaFXApp javaFXApp;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private FontIcon maxMinControlIcon;
    @FXML
    private AnchorPane mainContainer;
    @FXML
    private Modal modalController;

    public void setContent(Node node) {
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(node);
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
    }

    public void hideModal() {
        modalController.setVisible(false);
    }

    public void showModal(Node node) {
        modalController.setContent(node, true);
        modalController.setVisible(true);
    }

    public void showWarning(String message, Runnable onConfirm) {
        modalController.setContent(javaFXApp.getControlledNode("warning", (Warning controller) -> {
            controller.initData(message, () -> {
                onConfirm.run();
                modalController.setVisible(false);
            }, () -> modalController.setVisible(false));
        }), false);
    }

    @FXML
    public void handleTitleBarPressed(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (javaFXApp.maximize()) {
                maxMinControlIcon.setIconLiteral("mdal-call_to_action");
            }
            else {
                maxMinControlIcon.setIconLiteral("mdal-check_box_outline_blank");
            }
            return;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!stage.isMaximized()) {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }
    }

    @FXML
    public void handleTitleBarDragged(MouseEvent event) {
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
    public void handleMinimize(Event event) {
        javaFXApp.minimize();
    }

    @FXML
    public void handleMaximize(Event event) {
        if (javaFXApp.maximize()) {
            maxMinControlIcon.setIconLiteral("mdal-call_to_action");
        }
        else {
            maxMinControlIcon.setIconLiteral("mdal-check_box_outline_blank");
        }
    }
}
