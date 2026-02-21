package org.banew.hdh.fxapp.ui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ControllerUtils {

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
                        FadeTransition ft = new FadeTransition(Duration.millis(50), node);
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

    public static void processAvatar(ImageView topLabelAvatar, String url, int size) {
        double radius = (double) size / 2;
        Circle clip = new Circle(radius, radius, radius);
        topLabelAvatar.setClip(clip);
        topLabelAvatar.setFitWidth(size);
        topLabelAvatar.setFitHeight(size);
        topLabelAvatar.setPreserveRatio(false);
        topLabelAvatar.setImage(new Image(url));
    }
}
