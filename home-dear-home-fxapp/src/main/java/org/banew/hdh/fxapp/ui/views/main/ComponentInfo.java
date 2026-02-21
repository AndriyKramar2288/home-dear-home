package org.banew.hdh.fxapp.ui.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.banew.hdh.fxapp.ui.views.primary.PropertyView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ComponentInfo {

    private final JavaFXApp javaFXApp;

    @FXML
    private Pane actualComponentInfoBox;
    @FXML
    private Label componentName;
    @FXML
    private Label componentClass;
    @FXML
    private Label componentDescription;
    @FXML
    private Pane componentNameBox;
    @FXML
    private Pane argumentsBox;
    @FXML
    private Pane propertiesBox;
    @FXML
    private Pane propertiesArea;
    @FXML
    private ToggleGroup generationProcessingGroup;
    @FXML
    private RadioButton generationRadio;
    @FXML
    private RadioButton processingRadio;

    private LocationComponentAttributes currentAttributes;

    public void initialize() {
        resetAndShow();
        hide();
        generationProcessingGroup.selectedToggleProperty().addListener((
                observable, oldValue, newValue) -> {
            refreshArguments();
        });
    }

    public void hide() {
        actualComponentInfoBox.setVisible(false);
    }

    public void showExistComponent(LocationComponentDto info) {
        resetAndShow();
        componentNameBox.setVisible(true);
        componentNameBox.setManaged(true);
        actualComponentInfoBox.setVisible(true);
        componentName.setText(info.name());
        componentClass.setText(info.fullClassName());
        currentAttributes = info.classAttributes();
        refreshArguments();
    }

    public void showAvailableComponent(AvailableComponent availableComponent) {
        resetAndShow();
        currentAttributes = availableComponent.attributes();
        refreshArguments();
    }

    private void refreshArguments() {
        if (currentAttributes == null) {
            argumentsBox.setVisible(false);
            argumentsBox.setManaged(false);
            propertiesArea.setVisible(false);
            propertiesArea.setManaged(false);
            return;
        }

        propertiesBox.getChildren().clear();
        propertiesArea.setVisible(currentAttributes.properties().length != 0);
        propertiesArea.setManaged(currentAttributes.properties().length != 0);
        loadArguments(propertiesBox, currentAttributes.properties());

        generationRadio.setDisable(!currentAttributes.supportGeneration());
        processingRadio.setDisable(!currentAttributes.supportProcessing());

        Toggle selectedToggle = generationProcessingGroup.getSelectedToggle();
        if (selectedToggle == null || ((RadioButton) selectedToggle).isDisabled()) {
            argumentsBox.setVisible(false);
            argumentsBox.setManaged(false);
            return;
        }

        RadioButton rb = (RadioButton) selectedToggle;
        argumentsBox.setVisible(true);
        argumentsBox.setManaged(true);
        argumentsBox.getChildren().clear();

        String text = rb.getText();
        if ("GENERATION".equals(text)) {
            loadArguments(argumentsBox, currentAttributes.generationArguments());
        } else {
            loadArguments(argumentsBox, currentAttributes.processingArguments());
        }
    }

    private void loadArguments(Pane box, LocationComponentAttributes.Argument[] arguments) {

        boolean empty = arguments.length == 0;
        box.setVisible(!empty);
        box.setManaged(!empty);

        if (empty) {
            return;
        }

        for (LocationComponentAttributes.Argument argument : arguments) {
            box.getChildren().add(javaFXApp.getControlledNode("propertyView", (PropertyView c) -> {
                c.initData(argument);
            }));
        }
    }

    private void resetAndShow() {
        actualComponentInfoBox.setVisible(true);
        generationRadio.setDisable(true);
        processingRadio.setDisable(true);
        componentNameBox.setVisible(false);
        componentNameBox.setManaged(false);
        argumentsBox.setVisible(false);
        argumentsBox.setManaged(false);
    }
}
