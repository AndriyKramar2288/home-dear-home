package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
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

    public void initialize() {
        locationActualCard.setVisible(false);
        locationHolderCard.setVisible(true);
    }

    public void initData(LocationInfo locationInfo) {
        isInitialized = true;
        locationHolderCard.setVisible(false);
        locationActualCard.setVisible(true);
        componentsCountLabel.setText("Components: " + locationInfo.components().size());
        actionsCountLabel.setText("Actions: " + locationInfo.actions().size());
        locationNameLabel.setText(locationInfo.name());
    }
}
