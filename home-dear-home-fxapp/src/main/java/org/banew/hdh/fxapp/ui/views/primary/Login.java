package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.services.UserService;
import org.banew.hdh.core.api.layers.services.dto.LoginForm;
import org.banew.hdh.fxapp.implementations.services.AsyncRunner;
import org.springframework.stereotype.Component;

import static org.banew.hdh.fxapp.ui.ControllerUtils.showTimedAlert;

@Component
@RequiredArgsConstructor
public class Login {

    private final UserService userService;
    private final Primary primary;
    private final AsyncRunner asyncRunner;

    @FXML
    private Pane regularLoginForm;
    @FXML
    private Label formAlertLabel;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    public void setVisible(boolean visible) {
        regularLoginForm.setVisible(visible);
    }

    public void initialize() {
        loginField.setText("Petya");
        passwordField.setText("12345678");
        login(null);
    }

    @FXML
    public void switchToRegisterForm(ActionEvent actionEvent) {
        primary.setCurrentState(Primary.PrimaryState.REGISTRATION);
    }

    @FXML
    public void login(ActionEvent event) {
        asyncRunner.future(() -> userService.login(new LoginForm(loginField.getText(), passwordField.getText())), u -> {
            primary.setCurrentState(Primary.PrimaryState.LOCATION_CHOOSE);
        }, e -> {
            alertLogin(e.getMessage());
        });
    }

    private void alertLogin(String text) {
        showTimedAlert(formAlertLabel, text, 5);
    }
}
