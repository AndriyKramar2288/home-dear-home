package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class PropertyView {
    @FXML
    private Label propertyName;
    @FXML
    private Label propertyDescription;
    @FXML
    private Label propertyRequired;
    @FXML
    private Pane propertyValueBox;
    @FXML
    private TextField propertyValue;
    @FXML
    private ComboBox<String> propertyChoice;

    private Consumer<String> valueCallback;

    public void initialize() {
        propertyValueBox.setManaged(false);
        propertyValueBox.setVisible(false);

        propertyValue.setVisible(false);
        propertyChoice.setVisible(false);

        propertyChoice.valueProperty().addListener((
                observable, oldValue, newValue) -> {
            if (valueCallback != null) {
                valueCallback.accept(newValue);
            }
        });
        propertyValue.textProperty().addListener((
                observable, oldValue, newValue) -> {
            if (valueCallback != null) {
                valueCallback.accept(newValue);
            }
        });
    }

    public void setEditable(boolean editable) {
        propertyValueBox.setVisible(editable);
        propertyValueBox.setManaged(editable);
    }

    public void initData(LocationComponentAttributes.Argument argument,
                         String currentValue,
                         Consumer<String> valueCallback) {

        this.valueCallback = valueCallback;

        if (argument.choices().length > 0) {
            propertyChoice.getItems().clear();
            propertyChoice.getItems().addAll(argument.choices());
            propertyChoice.setValue(currentValue);
            propertyChoice.setVisible(true);
        }
        else {
            propertyValue.setText(currentValue);
            propertyValue.setVisible(true);
        }

        initData(argument);
    }

    public void initData(LocationComponentAttributes.Argument argument) {
        propertyName.setText(argument.name());
        propertyDescription.setText(argument.desc());
        propertyRequired.setText(argument.required() ? "REQUIRED" : "OPTIONAL");
        propertyRequired.setStyle("-fx-padding: 4; -fx-border-radius: 6; -fx-border-color: "
                + (argument.required() ? "#FF0000" : "#0000FF"));
    }
}
