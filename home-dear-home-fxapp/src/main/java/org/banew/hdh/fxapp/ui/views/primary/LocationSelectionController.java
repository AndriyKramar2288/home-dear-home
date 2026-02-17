package org.banew.hdh.fxapp.ui.views.primary;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class LocationSelectionController {

    @Setter
    private Consumer<PrimaryController.PrimaryState> changeState;
    @FXML
    private Pane locationChooseForm;

    public void setVisible(boolean visible) {
        locationChooseForm.setVisible(visible);
    }
}
