package org.banew.hdh.fxapp.ui.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.dto.LocationComponentInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ComponentInfo {
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

    public void initialize() {
        actualComponentInfoBox.setVisible(false);
    }

    public void hide() {
        actualComponentInfoBox.setVisible(false);
    }

    public void showAvailableComponent(Class<? extends LocationComponent<ComponentsContext>> clazz) {
        componentNameBox.setVisible(false);
        componentNameBox.setManaged(false);
        actualComponentInfoBox.setVisible(true);
        var attributes = clazz.getAnnotation(LocationComponentAttributes.class);
        if (attributes != null) {
            componentDescription.setText(attributes.description());
        }
    }

    public void showExistComponent(LocationComponentInfo info) {
        componentNameBox.setVisible(true);
        componentNameBox.setManaged(true);
        actualComponentInfoBox.setVisible(true);
        componentName.setText(info.name());
        componentClass.setText(info.fullClassName());
        if (info.classAttributes() != null) {
            componentDescription.setText(info.classAttributes().description());
        }
    }
}
