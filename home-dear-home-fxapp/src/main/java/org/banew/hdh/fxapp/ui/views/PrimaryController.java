package org.banew.hdh.fxapp.ui.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.runtime.forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Component
public class PrimaryController extends AbstractController {

    @Autowired
    private UserService userService;

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
    private Label topLabel;
    @FXML
    private Rectangle topLabelStick;

    @FXML
    private Pane regularLoginForm;
    @FXML
    private Pane registrationForm;
    @FXML
    private Pane locationChooseForm;

    @FXML
    private ImageView avatarPreview;
    @FXML
    private Node avatarImageBlock;
    @FXML
    private ImageView topLabelAvatar;

    //    REGISTRATION FIELDS
    @FXML
    private TextField registrationEmailField;
    @FXML
    private TextField registrationUsernameField;
    @FXML
    private PasswordField registrationPasswordField;
    @FXML
    private PasswordField registrationRepeatPasswordField;
    private File registrationAvatarImage = null;
    @FXML
    private Label registrationErrorLabel;

    private PrimaryState currentState;

    @Getter
    @AllArgsConstructor
    private enum PrimaryState {
        LOGIN,
        REGISTRATION,
        LOCATION_CHOOSE;
    }

    record StateConfig(Node form, String title, int fontSize) {}

    private void setCurrentState(PrimaryState currentState) {
        this.currentState = currentState;
        clickSound.play();

        var config = getStateConfig(currentState);

        config.form().toFront();

        if (config.title() != null) {
            topLabel.setText(config.title());
            topLabel.setStyle("-fx-font-size: " + config.fontSize() + ";");
        }

        topLabelAvatar.setVisible(false);
        topLabelAvatar.setManaged(false);

        if (currentState == PrimaryState.LOCATION_CHOOSE && userService.getCurrentUser().photoSrc() != null) {
            topLabelAvatar.setImage(new Image(userService.getCurrentUser().photoSrc()));
            topLabelAvatar.setVisible(true);
            topLabelAvatar.setManaged(true);
        }
    }

    private StateConfig getStateConfig(PrimaryState state) {
        return switch (state) {
            case LOGIN -> new StateConfig(regularLoginForm, "Hello!", 96);
            case REGISTRATION -> new StateConfig(registrationForm, "Account creation", 60);
            case LOCATION_CHOOSE -> new StateConfig(locationChooseForm, null, 0);
            default -> throw new IllegalStateException("Unexpected state: " + state);
        };
    }

    @FXML
    public void sendToDiscord(MouseEvent event) {
        javaFXApp.openWebpage("https://google.com");
    }

    @FXML
    public void sendToGithub(MouseEvent event) {
        javaFXApp.openWebpage("https://google.com");
    }

    public void initialize() {
        formAlertLabel.setManaged(false);
        registrationErrorLabel.setManaged(false);
        setUpClock();
        setUpDragAndDropAvatarImage(avatarImageBlock);
        metaInfoLabel.setText(String.format("HomeDearHome\nJavaFX %s", javaFXApp.getAppVersion()));
        topLabelStick.widthProperty().bind(topLabel.widthProperty());

        registrationForm.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setUpEverythingSmooth(newScene.getRoot());
            }
        });

        setCurrentState(PrimaryState.LOGIN);
    }

    @FXML
    public void switchToRegisterForm(ActionEvent event) {
        setCurrentState(PrimaryState.REGISTRATION);
    }

    @FXML
    public void login(ActionEvent event) {
        future(userService.login(new LoginForm(loginField.getText(), passwordField.getText())), u -> {
            setCurrentState(PrimaryState.LOCATION_CHOOSE);
        }, e -> {
            alertLogin(e.getMessage());
            throw new RuntimeException(e);
        });
    }

    @FXML
    public void switchToRegularLogin(ActionEvent event) {
        setCurrentState(PrimaryState.LOGIN);
    }

    @FXML
    public void attachImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your avatar image");

        // Фільтр, щоб не напхали сюди .exe або .obj
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Відкриваємо діалог (беремо Stage з будь-якого елемента сцени)
        File selectedFile = fileChooser.showOpenDialog(avatarPreview.getScene().getWindow());

        if (selectedFile != null) {
            successLoadImage(selectedFile);
        }
    }

    @FXML
    public void createAccountHandler(ActionEvent event) throws IOException {

        Consumer<String> callback = (imageUri) -> {
            future(userService.register(new RegisterForm(
                    registrationUsernameField.getText(),
                    registrationPasswordField.getText(),
                    registrationRepeatPasswordField.getText(),
                    registrationEmailField.getText(),
                    imageUri
            )), u -> {
                setCurrentState(PrimaryState.LOCATION_CHOOSE);
            }, e -> {
                alertRegister(e.getMessage());
            });
        };

        if (registrationAvatarImage != null) {
            future(userService.saveAvatarImage(Files.readAllBytes(Paths.get(registrationAvatarImage.toURI())),
                    registrationAvatarImage.getName()), callback, e -> {
                alertRegister(e.getMessage());
            });
        }
        else {
            callback.accept(null);
        }
    }

    private void alertLogin(String text) {
        showTimedAlert(formAlertLabel, text, 5);
    }

    private void alertRegister(String text) {
        showTimedAlert(registrationErrorLabel, text, 5);
    }

    private void setUpDragAndDropAvatarImage(Node node) {
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
                    successLoadImage(file);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void successLoadImage(File file) {
        registrationAvatarImage = file;

        Image image = new Image(file.toURI().toString());
        avatarPreview.setImage(image);

        System.out.println("Фото завантажено: " + file.getAbsolutePath());
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