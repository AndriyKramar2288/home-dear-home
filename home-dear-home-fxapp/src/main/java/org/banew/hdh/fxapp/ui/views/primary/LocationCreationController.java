package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LocationCreationController {

    @Autowired
    private LocationService<ComponentsContext> locationService;

    @FXML
    private TextField locationName;
    @FXML
    private TextArea locationDescription;
    @Setter
    private Runnable onLocationCreated;

    @FXML
    public void createLocation(ActionEvent event) {
        locationService.createLocation(locationName.getText(), locationDescription.getText());
        onLocationCreated.run();
    }
}