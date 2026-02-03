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
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.users.User;
import org.banew.hdh.core.api.users.forms.LoginForm;
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
    private HBox testPane;

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
        metaInfoLabel.setText(String.format("HomeDearHome\nJavaFX %s",
                javaFXApp.getAppVersion()));
    }

    @FXML
    public void login(ActionEvent event) {
        userService.login(new LoginForm() {
            @Override
            public String username() {
                return loginField.getText();
            }

            @Override
            public String password() {
                return passwordField.getText();
            }
        }).thenAccept(user -> {
            Platform.runLater(() -> {
                passwordField.setText(user.getUsername());
            });
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