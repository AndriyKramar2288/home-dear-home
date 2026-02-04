package org.banew.hdh.fxapp.ui.views;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.users.forms.LoginForm;
import org.banew.hdh.fxapp.ui.MyStyles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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