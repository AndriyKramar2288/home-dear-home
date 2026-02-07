package org.banew.hdh.fxapp.ui.views;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractController {

    @Autowired
    protected JavaFXApp javaFXApp;

    protected final AudioClip clickSound = new AudioClip(
            getClass().getResource("/views/assets/sounds/click_sound.mp3").toExternalForm()
    );

    protected void setUpEverythingSmooth(Parent root) {
        Platform.runLater(() -> {
            for (Node node : root.lookupAll("*")) { // Беремо всі елементи
                node.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
                    if (isVisible) {
                        node.setTranslateY(5);
                        TranslateTransition tt = new TranslateTransition(Duration.millis(100), node);
                        tt.setToY(0);
                        tt.play();
                    }
                });
            }
        });
    }

    protected <T> void future(CompletableFuture<T> future, Consumer<T> success, Consumer<Exception> failure) {
        future.thenAccept(t -> {
                    Platform.runLater(() -> {
                        success.accept(t);
                    });
                }).exceptionally(e -> {
                    // Витягуємо реальну причину (cause) з обгортки
                    Throwable cause = (e.getCause() != null) ? e.getCause() : e;
                    Platform.runLater(() -> {
                        failure.accept((Exception) cause);
                    });
                    return null; // exceptionally має щось повернути
                });
    }

    protected void showTimedAlert(Label alertLabel, String message, double seconds) {
        alertLabel.setText(message);
        alertLabel.setVisible(true);
        alertLabel.setManaged(true);

        // Створюємо паузу
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));

        // Що зробити, коли час вийде
        pause.setOnFinished(event -> {
            alertLabel.setVisible(false);
            alertLabel.setManaged(false);
        });

        pause.play(); // Запускаємо таймер
    }

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
