package org.banew.hdh.fxapp.ui.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import org.banew.hdh.core.api.dto.LocationComponentInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class ComponentsDiscovery {
    @FXML
    private ListView<LocationComponentInfo> componentsListView;

    public void initialize() {
        componentsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(LocationComponentInfo info, boolean empty) {
                super.updateItem(info, empty);
                if (empty || info == null) {
//                    setText(null);
//                    setGraphic(null);
                } else {
                    HBox root = new HBox(new Label(info.name()));
                    setGraphic(root);
                }
            }
        });
    }

    public void initData(Collection<? extends LocationComponentInfo> infos) {
        componentsListView.getItems().addAll(infos);
    }
}
