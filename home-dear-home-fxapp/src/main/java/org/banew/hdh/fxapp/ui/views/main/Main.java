package org.banew.hdh.fxapp.ui.views.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.banew.hdh.core.api.layers.services.LocationService;
import org.banew.hdh.core.api.layers.services.UserService;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.banew.hdh.fxapp.ui.ControllerUtils;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.banew.hdh.fxapp.ui.views.Modal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Main {

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private Modal modal;
    @Autowired
    private JavaFXApp javaFXApp;

    @FXML
    private ImageView avatarImage;
    @FXML
    private Label locationNameLabel;

    private LocationDto currentLocation;

    @FXML
    public void openComponentsButton(ActionEvent event) {
        modal.setContent(javaFXApp.getControlledNode("componentsDiscovery", (ComponentsDiscovery c) -> {
            c.initData(currentLocation.components(), locationService.getAvailableComponents());
        }), true);
    }

    public void initLocation(LocationDto locationDto) {
        currentLocation = locationDto;

        locationNameLabel.setText(locationDto.name());
        if (userService.getCurrentUser() != null && userService.getCurrentUser().photoSrc() != null) {
            ControllerUtils.processAvatar(avatarImage, userService.getCurrentUser().photoSrc(), 40);
        }
    }
}
