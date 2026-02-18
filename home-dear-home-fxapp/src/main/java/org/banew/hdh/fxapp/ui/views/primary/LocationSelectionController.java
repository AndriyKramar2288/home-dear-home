package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
public class LocationSelectionController {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaFXApp javaFXApp;

    @Setter
    private Consumer<PrimaryController.PrimaryState> changeState;
    @FXML
    private Pane locationChooseForm;
    @FXML
    private HBox locationList;

    public void setVisible(boolean visible) {
        locationChooseForm.setVisible(visible);
        var user = userService.getCurrentUser();
        if (user != null) {
            user.locations().forEach(location -> {
                try {
                    var loader = javaFXApp.getLoader("primary/locationCard");
                    Parent card = loader.load();
                    LocationCardController controller = loader.getController();
                    controller.initData(location);
                    locationList.getChildren().add(card);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
