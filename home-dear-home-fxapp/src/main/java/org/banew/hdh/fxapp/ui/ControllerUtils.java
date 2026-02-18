package org.banew.hdh.fxapp.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ControllerUtils {

    private static final AudioClip clickSound = new AudioClip(
            ControllerUtils.class.getResource("/views/assets/sounds/click_sound.mp3").toExternalForm()
    );

    public static void clickSound() {
        clickSound.play();
    }

    public static void setUpSmooth(Node anyNode) {
        anyNode.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setUpEverythingSmooth(newScene.getRoot());
            }
        });
    }

    private static void setUpEverythingSmooth(Parent root) {
        Platform.runLater(() -> {
            for (Node node : root.lookupAll("*")) { // Беремо всі елементи
                node.visibleProperty().addListener((obs, wasVisible, isVisible) -> {
                    if (isVisible) {
                        node.setOpacity(0.25);
                        FadeTransition ft = new FadeTransition(Duration.millis(100), node);
                        ft.setToValue(1);
                        ft.play();
                    }
                });
            }
        });
    }

    public static void setUpDragAndDropAvatarImage(Node node, Consumer<File> onSuccess) {
        node.setOnDragOver(event -> {
            // Перевіряємо, чи перетягують саме файли
            if (event.getGestureSource() != node && event.getDragboard().hasFiles()) {
                // Дозволяємо копіювання
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // 2. Файлик увійшов (Drag Entered) — для візуальних ефектів
        node.setOnDragEntered(event -> {
            if (event.getGestureSource() != node && event.getDragboard().hasFiles()) {
                // Можеш додати стиль підсвітки (наприклад, через твій CSS клас)
                node.setStyle("-fx-background-color: rgba(100, 100, 100, 0.25);");
            }
            event.consume();
        });

        // 3. Файлик вийшов (Drag Exited) — прибираємо ефекти
        node.setOnDragExited(event -> {
            node.setStyle(""); // Повертаємо дефолтний стиль
            event.consume();
        });

        // 4. Файлик відпустили (Drag Dropped) — обробка результату
        node.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                // Беремо перший файл зі списку
                File file = db.getFiles().get(0);

                // Перевіряємо, чи це картинка (проста перевірка розширення)
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                    onSuccess.accept(file);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    public static <T> void future(CompletableFuture<T> future, Consumer<T> success, Consumer<Exception> failure) {
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

    public static void showTimedAlert(Label alertLabel, String message, double seconds) {
        alertLabel.setText(message);
        alertLabel.setVisible(true);
        alertLabel.setManaged(true);

        // Створюємо паузу
        PauseTransition pause = new PauseTransition(Duration.seconds(seconds));

        // Що зробити, коли час вийде
        pause.setOnFinished(event -> {
            alertLabel.setVisible(false);
        });

        pause.play(); // Запускаємо таймер
    }
}
