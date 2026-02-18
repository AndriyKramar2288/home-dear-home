package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LocationSelectionController {

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService<ComponentsContext> locationService;
    @Autowired
    private JavaFXApp javaFXApp;

    @Setter
    private PrimaryController primaryController;
    @FXML
    private Pane locationChooseForm;
    @FXML
    private HBox locationList;

    public void setVisible(boolean visible) {
        locationChooseForm.setVisible(visible);
        updateList();
    }

    private void updateList() {
        var user = userService.getCurrentUser();
        if (user != null) {
            locationList.getChildren().clear();
            user.locations().forEach(location -> {
                try {
                    var loader = javaFXApp.getLoader("primary/locationCard");
                    Parent card = loader.load();
                    LocationCardController controller = loader.getController();
                    controller.initData(location, () -> {
                    }, () -> {
                        primaryController.showWarning("Are you sure you want to delete this location?", () -> {
                            locationService.removeLocation(location.id());
                            updateList();
                        });
                    });
                    locationList.getChildren().add(card);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
