package org.banew.hdh.fxapp.ui.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Warning {

    @FXML
    private Label messageLabel;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public void initData(String message, Runnable confirm, Runnable cancel) {
        messageLabel.setText(message);
        confirmButton.setOnAction(event -> confirm.run());
        cancelButton.setOnAction(event -> cancel.run());
    }
}