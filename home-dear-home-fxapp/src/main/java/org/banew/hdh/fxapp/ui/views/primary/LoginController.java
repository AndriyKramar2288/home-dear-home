package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.fxapp.ui.views.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static org.banew.hdh.fxapp.ui.ControllerUtils.future;
import static org.banew.hdh.fxapp.ui.ControllerUtils.showTimedAlert;

@Component
public class LoginController {

    @Autowired
    private UserService userService;

    @FXML
    private Pane regularLoginForm;

    @Setter
    private Consumer<PrimaryController.PrimaryState> changeState;

    public void setVisible(boolean visible) {
        regularLoginForm.setVisible(visible);
    }

    @FXML
    public void switchToRegisterForm(ActionEvent actionEvent) {
        changeState.accept(PrimaryController.PrimaryState.REGISTRATION);
    }

    @FXML
    public void login(ActionEvent event) {
        future(userService.login(new LoginForm(loginField.getText(), passwordField.getText())), u -> {
            changeState.accept(PrimaryController.PrimaryState.LOCATION_CHOOSE);
        }, e -> {
            alertLogin(e.getMessage());
        });
    }

    private void alertLogin(String text) {
        showTimedAlert(formAlertLabel, text, 5);
    }

    @FXML
    private Label formAlertLabel;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
}
