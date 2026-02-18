package org.banew.hdh.fxapp.ui.views.primary;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.fxapp.ui.ControllerUtils;
import org.banew.hdh.fxapp.ui.views.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.banew.hdh.fxapp.ui.ControllerUtils.setUpSmooth;

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
    private Label topLabel;
    @FXML
    private Rectangle topLabelStick;
    @FXML
    private LoginController loginController;
    @FXML
    private RegistrationController registrationController;
    @FXML
    private LocationSelectionController locationSelectionController;
    @FXML
    private ImageView topLabelAvatar;

    @AllArgsConstructor
    public enum PrimaryState {
        LOGIN,
        REGISTRATION,
        LOCATION_CHOOSE;
    }

    public void setCurrentState(PrimaryState state) {
        ControllerUtils.clickSound();
        loginController.setVisible(state == PrimaryState.LOGIN);
        registrationController.setVisible(state == PrimaryState.REGISTRATION);
        locationSelectionController.setVisible(state == PrimaryState.LOCATION_CHOOSE);

        switch (state) {
            case LOGIN -> updateTopLabel("Hello!", 96, false);
            case REGISTRATION -> updateTopLabel("Account creation", 60, false);
            case LOCATION_CHOOSE -> updateTopLabel("Choose Home", 60, true);
        }
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
        setUpClock();
        setUpSmooth(topLabel);
        topLabelAvatar.setVisible(false);
        topLabelAvatar.setManaged(false);

        loginController.setPrimaryController(this);
        registrationController.setPrimaryController(this);
        locationSelectionController.setPrimaryController(this);

        metaInfoLabel.setText(String.format("HomeDearHome\nJavaFX %s", javaFXApp.getAppVersion()));
        topLabelStick.widthProperty().bind(topLabel.widthProperty());

        setCurrentState(PrimaryState.LOGIN);
    }

    private void updateTopLabel(String text, int size, boolean showAvatar) {
        topLabel.setText(text);
        topLabel.setStyle("-fx-font-size: " + size + ";");
        boolean hasPhoto = userService.getCurrentUser() != null && userService.getCurrentUser().photoSrc() != null;
        topLabelAvatar.setVisible(showAvatar && hasPhoto);
        topLabelAvatar.setManaged(showAvatar && hasPhoto);
        if (topLabelAvatar.isVisible()) {
            double radius = 50;
            Circle clip = new Circle(radius, radius, radius);
            topLabelAvatar.setClip(clip);
            topLabelAvatar.setFitWidth(100);
            topLabelAvatar.setFitHeight(100);
            topLabelAvatar.setPreserveRatio(false);
            topLabelAvatar.setImage(new Image(userService.getCurrentUser().photoSrc()));
        }
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