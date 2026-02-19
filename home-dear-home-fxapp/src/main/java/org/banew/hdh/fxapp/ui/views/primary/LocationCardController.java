package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.dto.LocationInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LocationCardController {

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

    private LocationCardController locationCardController;

    public void initialize() {
        locationActualCard.setVisible(false);
        locationHolderCard.setVisible(true);
    }

    public void initData(LocationInfo locationInfo, Runnable onConfirm, Runnable onDelete) {
        deleteLocationButton.setOnAction(actionEvent -> onDelete.run());
        runLocationButton.setOnAction(actionEvent -> onConfirm.run());
        isInitialized = true;
        locationHolderCard.setVisible(false);
        locationActualCard.setVisible(true);
        componentsCountLabel.setText("Components: " + locationInfo.components().size());
        actionsCountLabel.setText("Actions: " + locationInfo.actions().size());
        locationNameLabel.setText(locationInfo.name());
    }
}
