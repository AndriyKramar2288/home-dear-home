package org.banew.hdh.fxapp.ui.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.banew.hdh.core.api.dto.LocationComponentInfo;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Scope("prototype")
public class ComponentsDiscovery {
    @FXML
    private ListView<Class<? extends LocationComponent<ComponentsContext>>> newComponentsListView;
    @FXML
    private ListView<LocationComponentInfo> addedComponentsListView;
    @FXML
    private ComponentInfo componentInfoController;
    @FXML
    private ToggleGroup tabRadioButtonGroup;

    public void initialize() {
        tabRadioButtonGroup.selectedToggleProperty().addListener((
                observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton rb = (RadioButton) newValue;
                componentInfoController.hide();

                if (rb.getAccessibleText().equals("ADDED")) {
                    addedComponentsListView.setVisible(true);
                    newComponentsListView.setVisible(false);
                }
                else {
                    newComponentsListView.setVisible(true);
                    addedComponentsListView.setVisible(false);
                }
            }
        });

        addedComponentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(LocationComponentInfo info, boolean empty) {
                super.updateItem(info, empty);
                if (!empty && info != null) {
                    HBox root = new HBox(new Label(info.name()));
                    setGraphic(root);
                    root.setOnMouseClicked(event -> componentInfoController.showExistComponent(info));
                }
            }
        });

        newComponentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Class<? extends LocationComponent<ComponentsContext>> info, boolean empty) {
                super.updateItem(info, empty);
                if (!empty && info != null) {
                    var annotation = info.getAnnotation(LocationComponentAttributes.class);
                    HBox root = new HBox(new Label(annotation == null ? info.getName() : annotation.name()));
                    setGraphic(root);
                    root.setOnMouseClicked(event -> componentInfoController.showAvailableComponent(info));
                }
            }
        });
    }

    public void initData(Collection<? extends LocationComponentInfo> infos,
                         Collection<Class<? extends LocationComponent<ComponentsContext>>> classes) {
        addedComponentsListView.getItems().addAll(infos);
        newComponentsListView.getItems().addAll(classes);
    }
}
