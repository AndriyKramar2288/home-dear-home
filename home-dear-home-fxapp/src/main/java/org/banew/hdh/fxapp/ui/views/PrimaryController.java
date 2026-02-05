package org.banew.hdh.fxapp.ui.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.fxapp.ui.MyStyles;
import org.fxyz3d.importers.Model3D;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.fxyz3d.importers.Importer3D;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PrimaryController extends AbstractController {

    @Autowired
    private UserService userService;

    @FXML
    private Pane back;
    @FXML
    private Label clockLabel;
    @FXML
    private Label regionLabel;
    @FXML
    private Label metaInfoLabel;

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label formAlertLabel;

    @FXML
    public void sendToDiscord(MouseEvent event) {
        javaFXApp.openWebpage("https://google.com");
    }

    @FXML
    public void sendToGithub(MouseEvent event) {
        javaFXApp.openWebpage("https://google.com");
    }

    public void initialize() {
        setUpClock();
        metaInfoLabel.setText(String.format("HomeDearHome\nJavaFX %s", javaFXApp.getAppVersion()));

        Runnable runnable = () -> {
            formAlertLabel.setVisible(false);
            formAlertLabel.setManaged(false);
        };
        runnable.run();
        loginField.setOnKeyPressed(event -> runnable.run());
        passwordField.setOnKeyPressed(event -> runnable.run());
    }

    @FXML
    public void forgetPassword(ActionEvent event) {
        Group root3D = new Group(); // Твій ізольований 3D світ

        SubScene subScene = new SubScene(
                root3D,
                800, 600,
                true,
                SceneAntialiasing.BALANCED
        );
        subScene.setFill(Color.TRANSPARENT);
        subScene.widthProperty().bind(back.widthProperty());
        subScene.heightProperty().bind(back.heightProperty());

        // ДОДАЄМО ПОРТАЛ НА ФОН (першим у списку дітей, щоб він був позаду кнопок)
        back.getChildren().add(0, subScene);

        // 1. Створюємо камеру
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);

        // 2. Створюємо Pivot
        Group cameraPivot = new Group();
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        cameraPivot.getChildren().add(camera);
        cameraPivot.getTransforms().addAll(rotateY, rotateX);

        // !!! ВАЖЛИВО: Додаємо камеру та модель у root3D, а НЕ в back !!!
        root3D.getChildren().add(cameraPivot);
        Node model = loadMyModel(root3D); // Передаємо root3D сюди

        // !!! Світло теж тільки в 3D світ !!!
        root3D.getChildren().add(new AmbientLight(Color.color(0.3, 0.3, 0.3)));
        PointLight pointLight = new PointLight(Color.WHITE);
        pointLight.setTranslateZ(-1100);
        root3D.getChildren().add(pointLight);

        // 5. Вмикаємо камеру ТІЛЬКИ ДЛЯ SUBSCENE
        subScene.setCamera(camera);

        // ПРИБИРАЄМО scene.setCamera(camera) - вона ламає 2D!

        setupControls(clockLabel.getScene(), camera, rotateX, rotateY, model);
    }

    private void setupControls(Scene subScene, Camera camera, Rotate rotateX, Rotate rotateY, Node model) {

        // 4. Логіка керування мишкою
        final double[] mouseAnchor = new double[2];

        subScene.setOnMousePressed(mouseEvent -> {
            mouseAnchor[0] = mouseEvent.getSceneX();
            mouseAnchor[1] = mouseEvent.getSceneY();
        });

        subScene.setOnMouseDragged(mouseEvent -> {
            double deltaX = mouseEvent.getSceneX() - mouseAnchor[0];
            double deltaY = mouseEvent.getSceneY() - mouseAnchor[1];

            // Обертаємо: Y — вліво/вправо, X — вгору/вниз
            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.2);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.2);

            mouseAnchor[0] = mouseEvent.getSceneX();
            mouseAnchor[1] = mouseEvent.getSceneY();
        });

        // Zoom через коліщатко
        subScene.setOnScroll(scrollEvent -> {
            double zoom = camera.getTranslateZ() + scrollEvent.getDeltaY() * 2;
            if (zoom < -200 && zoom > -5000) {
                camera.setTranslateZ(zoom);
            }
        });

        subScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.W) {
                model.setTranslateX(model.getTranslateX() + 10);
            }
            if (keyEvent.getCode() == KeyCode.S) {
                model.setTranslateX(model.getTranslateX() - 10);
            }
            if (keyEvent.getCode() == KeyCode.A) {
                model.setTranslateY(model.getTranslateY() + 10);
            }
            if (keyEvent.getCode() == KeyCode.D) {
                model.setTranslateY(model.getTranslateY() - 10);
            }
            if (keyEvent.getCode() == KeyCode.CONTROL) {
                model.setTranslateZ(model.getTranslateZ() + 10);
            }
            if (keyEvent.getCode() == KeyCode.SHIFT) {
                model.setTranslateZ(model.getTranslateZ() - 10);
            }
        });
    }

    private Node loadMyModel(Group sceneRoot) {
        try {
            // 1. Вказуємо шлях до файлу (краще через URL ресурсів)
            URL modelUrl = getClass().getResource("/Texture_Shool/anime_school.obj");

            // 2. Завантажуємо
            Model3D model = Importer3D.load(modelUrl);

            // 3. Отримуємо Node (це буде Group, що містить MeshView)
            Node modelNode = model.getRoot();

            // 4. Важливо: налаштовуємо масштаб та позицію
            // Модельки з 3D редакторів часто приходять зі зсунутим центром
            modelNode.setScaleX(100.0);
            modelNode.setScaleY(100.0);
            modelNode.setScaleZ(100.0);
            modelNode.setRotate(180);

            // Додаємо в твій Pivot або прямо в Root
            sceneRoot.getChildren().add(modelNode);

            return modelNode;

        } catch (IOException e) {
            System.err.println("Баняк, моделька не знайшлась: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void register(ActionEvent event) {
        formAlertLabel.setVisible(false);
        formAlertLabel.setManaged(false);
        formAlertLabel.getScene().getRoot().pseudoClassStateChanged(MyStyles.REGISTER_MODE, true);
    }

    @FXML
    public void login(ActionEvent event) {
        userService.login(new LoginForm(loginField.getText(), passwordField.getText())) // Краще передати дані відразу
                .thenAccept(user -> {
                    Platform.runLater(() -> {
                        // Успіх! Можемо переходити на іншу локацію
                        passwordField.setText("Welcome, " + user.getUsername());
                    });
                }).exceptionally(e -> {
                    // Витягуємо реальну причину (cause) з обгортки
                    Throwable cause = (e.getCause() != null) ? e.getCause() : e;

                    Platform.runLater(() -> {
                        formAlertLabel.setText(cause.getMessage());
                        formAlertLabel.setVisible(true);
                        formAlertLabel.setManaged(true);
                    });
                    return null; // exceptionally має щось повернути
                });
    }

    private void setUpClock() {
        regionLabel.setText(ZonedDateTime.now().getZone().toString());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            String formattedTime = currentTime.format(dtf);
            clockLabel.setText(formattedTime);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}