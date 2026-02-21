package org.banew.hdh.fxapp.ui.views.main.component;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.banew.hdh.core.api.layers.services.LocationService;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class ComponentInfo {

    private final JavaFXApp javaFXApp;
    private final LocationService locationService;

    @FXML private Pane actualComponentInfoBox;
    @FXML @Getter private Label componentName;
    @FXML @Getter private Label componentClass;
    @FXML @Getter private Label componentDescription;
    @FXML @Getter private Pane componentNameBox;
    @FXML private Pane argumentsBox;
    @FXML private Pane propertiesBox;
    @FXML private Pane propertiesArea;
    @FXML private ToggleGroup generationProcessingGroup;
    @FXML private RadioButton generationRadio;
    @FXML private RadioButton processingRadio;

    @FXML private Button disableEditingButton; // Це кнопка Cancel
    @FXML private Button enableEditingButton;  // Це кнопка Edit (олівець)
    @FXML private Button saveEditingButton;    // Це кнопка Save

    private ComponentMode currentMode; // ЄДИНИЙ СТАН КОНТРОЛЕРА
    @Getter private Consumer<Boolean> lockOutside;

    // Кешуємо дані для перемальовування радіокнопок
    private LocationComponentAttributes currentAttributes;
    private Map<String, String> currentProperties;
    private boolean currentEditable;
    @Setter
    private String locationId;

    public void initialize() {
        hide();
        // Ліснери вішаємо ОДИН раз, а вони делегують події поточному режиму
        generationProcessingGroup.selectedToggleProperty().addListener((obs, oldV, newV) -> {
            if (currentMode != null) currentMode.onToggleChanged(this);
        });

        enableEditingButton.setOnAction(e -> { if (currentMode != null) currentMode.onEditClicked(this); });
        saveEditingButton.setOnAction(e -> { if (currentMode != null) currentMode.onSaveClicked(this); });
        disableEditingButton.setOnAction(e -> { if (currentMode != null) currentMode.onCancelClicked(this); });
    }

    public void hide() {
        actualComponentInfoBox.setVisible(false);
    }

    // --- ОСНОВНА ТОЧКА ВХОДУ ---
    public void setMode(ComponentMode mode) {
        this.currentMode = mode;
        this.actualComponentInfoBox.setVisible(true);
        mode.init(this, locationId); // Кажемо режиму: "Налаштуй мене!"
    }

    // Точки входу ззовні (з ListView)
    public void showExistComponent(LocationComponentDto info, Consumer<Boolean> lockOutside) {
        this.lockOutside = lockOutside;
        setMode(new ViewExistingMode(info, locationService));
    }

    public void showAvailableComponent(AvailableComponent available, Consumer<Boolean> lockOutside) {
        this.lockOutside = lockOutside;
        setMode(new ViewAvailableMode(available));
    }

    // --- МЕТОДИ ДЛЯ КЕРУВАННЯ UI (викликаються з Режимів) ---

    public void setupButtons(boolean canEdit, boolean isEditing) {
        enableEditingButton.setVisible(canEdit);
        enableEditingButton.setManaged(canEdit);
        disableEditingButton.setVisible(isEditing);
        disableEditingButton.setManaged(isEditing);
        saveEditingButton.setVisible(isEditing);
        saveEditingButton.setManaged(isEditing);
    }

    public void refreshArguments(LocationComponentAttributes attributes, Map<String, String> properties, boolean isEditable) {
        this.currentAttributes = attributes;
        this.currentProperties = properties;
        this.currentEditable = isEditable;

        propertiesBox.getChildren().clear();
        boolean hasProps = attributes.properties().length > 0;
        propertiesArea.setVisible(hasProps);
        propertiesArea.setManaged(hasProps);

        if (hasProps) {
            loadArguments(propertiesBox, attributes.properties(), properties, isEditable);
        }

        generationRadio.setDisable(!attributes.supportGeneration());
        processingRadio.setDisable(!attributes.supportProcessing());

        renderRadioArguments();
    }

    public void renderRadioArguments() {
        Toggle selectedToggle = generationProcessingGroup.getSelectedToggle();
        if (selectedToggle == null || ((RadioButton) selectedToggle).isDisabled() || currentAttributes == null) {
            argumentsBox.setVisible(false);
            argumentsBox.setManaged(false);
            return;
        }

        argumentsBox.setVisible(true);
        argumentsBox.setManaged(true);
        argumentsBox.getChildren().clear();

        String text = ((RadioButton) selectedToggle).getText();
        if ("GENERATION".equals(text)) {
            loadArguments(argumentsBox, currentAttributes.generationArguments(), null, false); // Аргументи завжди readonly
        } else {
            loadArguments(argumentsBox, currentAttributes.processingArguments(), null, false);
        }
    }

    private void loadArguments(Pane box, LocationComponentAttributes.Argument[] arguments, Map<String, String> values, boolean isEditable) {
        boolean empty = arguments.length == 0;
        box.setVisible(!empty);
        box.setManaged(!empty);
        if (empty) return;

        for (LocationComponentAttributes.Argument argument : arguments) {
            box.getChildren().add(javaFXApp.getControlledNode("propertyView", (PropertyView c) -> {
                if (values != null) {
                    c.initData(argument, values.get(argument.name()), (newValue) -> values.put(argument.name(), newValue));
                    c.setEditable(isEditable);
                } else  {
                    c.initData(argument);
                }
            }));
        }
    }

    public void resetUI() {
        generationRadio.setDisable(true);
        processingRadio.setDisable(true);
        argumentsBox.setVisible(false);
        argumentsBox.setManaged(false);
    }

    public void hideDynamicAreas() {
        argumentsBox.setVisible(false);
        argumentsBox.setManaged(false);
        propertiesArea.setVisible(false);
        propertiesArea.setManaged(false);
    }
}