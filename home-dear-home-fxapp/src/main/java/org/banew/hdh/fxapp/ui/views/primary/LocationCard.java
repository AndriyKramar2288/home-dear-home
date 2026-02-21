package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LocationCard {

    private boolean isInitialized = false;

    @FXML
    private Pane locationActualCard;
    @FXML
    private Pane locationHolderCard;
    @FXML
    private Label componentsCountLabel;
    @FXML
    private Label actionsCountLabel;
    @FXML
    private Label locationNameLabel;
    @FXML
    private Button deleteLocationButton;
    @FXML
    private Button runLocationButton;

    private LocationCard locationCard;

    public void initialize() {
        locationActualCard.setVisible(false);
        locationHolderCard.setVisible(true);
    }

    public void initData(LocationDto locationDto, Runnable onConfirm, Runnable onDelete) {
        deleteLocationButton.setOnAction(actionEvent -> onDelete.run());
        runLocationButton.setOnAction(actionEvent -> onConfirm.run());
        isInitialized = true;
        locationHolderCard.setVisible(false);
        locationActualCard.setVisible(true);
        componentsCountLabel.setText("Components: " + locationDto.components().size());
        actionsCountLabel.setText("Actions: " + locationDto.actions().size());
        locationNameLabel.setText(locationDto.name());
    }
}
