package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.banew.hdh.core.api.layers.services.dto.RegisterForm;
import org.banew.hdh.core.api.layers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static org.banew.hdh.fxapp.ui.ControllerUtils.*;

@Component
public class Registration {

    @Autowired
    private Primary primary;
    @Autowired
    private UserService userService;

    @FXML
    private TextField registrationEmailField;
    @FXML
    private TextField registrationUsernameField;
    @FXML
    private PasswordField registrationPasswordField;
    @FXML
    private PasswordField registrationRepeatPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView avatarPreview;
    @FXML
    private Node avatarImageBlock;
    @FXML
    private Pane registrationForm;

    private File registrationAvatarImage = null;

    public void setVisible(boolean visible) {
        registrationForm.setVisible(visible);
    }

    private void alertRegister(String text) {
        showTimedAlert(errorLabel, text, 5);
    }

    public void initialize() {
        setUpDragAndDropAvatarImage(avatarImageBlock, this::successLoadImage);
    }

    @FXML
    public void switchToRegularLogin(ActionEvent actionEvent) {
        primary.setCurrentState(Primary.PrimaryState.LOGIN);
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
                primary.setCurrentState(Primary.PrimaryState.LOCATION_CHOOSE);
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

    private void successLoadImage(File file) {
        registrationAvatarImage = file;
        Image image = new Image(file.toURI().toString());
        avatarPreview.setImage(image);
    }
}
