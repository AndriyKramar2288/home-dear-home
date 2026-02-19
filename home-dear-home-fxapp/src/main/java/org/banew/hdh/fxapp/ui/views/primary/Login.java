package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.banew.hdh.fxapp.ui.ControllerUtils.future;
import static org.banew.hdh.fxapp.ui.ControllerUtils.showTimedAlert;

@Component
public class Login {

    @Autowired
    private UserService userService;
    @Autowired
    private Primary primary;

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
        future(userService.login(new LoginForm(loginField.getText(), passwordField.getText())), u -> {
            primary.setCurrentState(Primary.PrimaryState.LOCATION_CHOOSE);
        }, e -> {
            alertLogin(e.getMessage());
        });
    }

    private void alertLogin(String text) {
        showTimedAlert(formAlertLabel, text, 5);
    }
}
