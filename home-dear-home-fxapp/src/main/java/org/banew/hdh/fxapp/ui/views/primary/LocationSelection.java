package org.banew.hdh.fxapp.ui.views.primary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.services.LocationService;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.fxapp.implementations.ComponentsContext;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.banew.hdh.fxapp.ui.views.Wrap;
import org.banew.hdh.fxapp.ui.views.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
public class LocationSelection {

    @Autowired
    private UserService userService;
    @Autowired
    private LocationService<ComponentsContext> locationService;
    @Autowired
    private JavaFXApp javaFXApp;
    @Autowired
    private Wrap wrap;

    @FXML
    private Pane locationChooseForm;
    @FXML
    private HBox locationList;

    @FXML
    public void createLocation(ActionEvent event) throws IOException {
        var loader = javaFXApp.getLoader("primary/locationCreation");
        Node node = loader.load();
        LocationCreation controller = loader.getController();
        controller.setOnLocationCreated(() -> {
            updateList();
            wrap.hideModal();
        });
        wrap.showModal(node);
    }

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
                    LocationCard controller = loader.getController();
                    controller.initData(location, () -> {
                        javaFXApp.changeScene("main", (Main m) -> {
                            m.initLocation(location);
                        });
                    }, () -> {
                        wrap.showWarning("Are you sure you want to delete this location?", () -> {
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
