package org.banew.hdh.fxapp.ui.views.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.banew.hdh.fxapp.ui.views.primary.PropertyView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    @FXML
    private Button disableEditingButton;
    @FXML
    private Button enableEditingButton;
    @FXML
    private Button saveEditingButton;

    private LocationComponentAttributes currentAttributes;
    private final Map<String, String> properties = new HashMap<>();
    private final Map<String, String> genAttributes = new HashMap<>();
    private final Map<String, String> procAttributes = new HashMap<>();
    private boolean isEditing = false;
    private boolean isActionAdding = false;
    private Consumer<Boolean> lockOutside;

    public void initialize() {
        resetAndShow();
        hide();
        generationProcessingGroup.selectedToggleProperty().addListener((
                observable, oldValue, newValue) -> {
            refreshArguments();
        });

        disableEditingButton.setVisible(false);
        disableEditingButton.setManaged(false);
        saveEditingButton.setVisible(false);
        saveEditingButton.setManaged(false);

        enableEditingButton.setOnAction(event -> {
            isEditing = true;
            lockOutside.accept(true);
            refreshArguments();

            enableEditingButton.setManaged(false);
            enableEditingButton.setDisable(false);
            disableEditingButton.setVisible(true);
            disableEditingButton.setManaged(true);
            saveEditingButton.setVisible(true);
            saveEditingButton.setManaged(true);
        });
    }

    public void hide() {
        actualComponentInfoBox.setVisible(false);
    }

    public void showExistComponent(LocationComponentDto info, Consumer<Boolean> lockOutside) {
        this.lockOutside = lockOutside;

        resetAndShow();
        componentNameBox.setVisible(true);
        componentNameBox.setManaged(true);
        actualComponentInfoBox.setVisible(true);
        componentName.setText(info.name());
        componentClass.setText(info.fullClassName());
        currentAttributes = info.classAttributes();
        properties.putAll(info.properties());
        refreshArguments();
    }

    public void showAvailableComponent(AvailableComponent availableComponent, Consumer<Boolean> lockOutside) {
        this.lockOutside = lockOutside;

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
        loadArguments(propertiesBox, currentAttributes.properties(), properties);

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
            loadArguments(argumentsBox, currentAttributes.generationArguments(), null);
        } else {
            loadArguments(argumentsBox, currentAttributes.processingArguments(), null);
        }
    }

    private void loadArguments(Pane box, LocationComponentAttributes.Argument[] arguments, Map<String, String> values) {

        boolean empty = arguments.length == 0;
        box.setVisible(!empty);
        box.setManaged(!empty);

        if (empty) {
            return;
        }

        for (LocationComponentAttributes.Argument argument : arguments) {
            box.getChildren().add(javaFXApp.getControlledNode("propertyView", (PropertyView c) -> {
                if (values != null) {
                    c.initData(argument,
                            values.get(argument.name()),
                            (newValue) -> values.put(argument.name(), newValue));
                    c.setEditable(isEditing);
                }
                else  {
                    c.initData(argument);
                }
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
