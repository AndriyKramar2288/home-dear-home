package org.banew.hdh.fxapp.ui.views.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.fxapp.implementations.services.ComponentsContextImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@Scope("prototype")
public class ComponentsDiscovery {
    @FXML
    private ListView<AvailableComponent> newComponentsListView;
    @FXML
    private ListView<LocationComponentDto> addedComponentsListView;
    @FXML
    private ComponentInfo componentInfoController;
    @FXML
    private ToggleGroup tabRadioButtonGroup;

    public void initialize() {

        addedComponentsListView.setVisible(true);
        newComponentsListView.setVisible(false);

        tabRadioButtonGroup.selectedToggleProperty().addListener((
                observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton rb = (RadioButton) newValue;
                componentInfoController.hide();

                if (rb.getAccessibleText().equals("ADDED")) {
                    addedComponentsListView.setVisible(true);
                    newComponentsListView.setVisible(false);
                    var sel = addedComponentsListView.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        componentInfoController.showExistComponent(sel);
                    }
                }
                else {
                    newComponentsListView.setVisible(true);
                    addedComponentsListView.setVisible(false);
                    var sel = newComponentsListView.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        componentInfoController.showAvailableComponent(sel);
                    }
                }
            }
        });

        setUpListView(addedComponentsListView,
                comp -> new HBox(new Label(comp.name())),
                comp -> componentInfoController.showExistComponent(comp),
                                    () -> componentInfoController.hide());

        setUpListView(newComponentsListView,
                comp -> new HBox(new Label(
                        comp.attributes() == null? comp.fullClassName() : comp.attributes().name())),
                comp -> componentInfoController.showAvailableComponent(comp),
                () -> componentInfoController.hide());
    }

    public void initData(Collection<? extends LocationComponentDto> infos,
                         List<AvailableComponent> classes) {
        addedComponentsListView.getItems().addAll(infos);
        newComponentsListView.getItems().addAll(classes);
    }

    private static <T> void setUpListView(ListView<T> list, Function<T, Node> nodeCreator, Consumer<T> onShow, Runnable onHide) {
        list.setOnMousePressed(event -> {
            if (event.getTarget() == list) {
                list.getSelectionModel().clearSelection();
            }
        });

        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(T info, boolean empty) {
                super.updateItem(info, empty);
                if (empty || info == null) {
                    setGraphic(null);
                    setOnMouseClicked(null);
                } else {
                    setGraphic(nodeCreator.apply(info));
                }
            }
        });

        list.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                onShow.accept(newSelection);
            }
            else {
                onHide.run();
            }
        });
    }
}
