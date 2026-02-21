package org.banew.hdh.fxapp.ui.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.implementations.services.ComponentsContextImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Scope("prototype")
public class ComponentsDiscovery {
    @FXML
    private ListView<LocationComponentAttributes> newComponentsListView;
    @FXML
    private ListView<LocationComponentDto> addedComponentsListView;
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
            protected void updateItem(LocationComponentDto info, boolean empty) {
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
            protected void updateItem(LocationComponentAttributes info, boolean empty) {
                super.updateItem(info, empty);
                if (!empty && info != null) {
                    HBox root = new HBox(new Label(info.name()));
                    setGraphic(root);
                    //root.setOnMouseClicked(event -> componentInfoController.showAvailableComponent(info));
                }
            }
        });
    }

    public void initData(Collection<? extends LocationComponentDto> infos,
                         List<LocationComponentAttributes> classes) {
        addedComponentsListView.getItems().addAll(infos);
        newComponentsListView.getItems().addAll(classes);
    }
}
