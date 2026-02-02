package org.banew.hdh.fxapp.abstractions;

import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.LocationComponent;

public interface WidgetLocationComponent extends LocationComponent {
    void init(Pane pane);
}
