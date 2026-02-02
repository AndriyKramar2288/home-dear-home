package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Action;
import org.banew.hdh.fxapp.abstractions.WidgetLocationComponent;
import org.banew.hdh.fxapp.implementations.widgets.AbstractWidgetLocationComponent;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLocation implements Location {

    @XmlElementWrapper(name = "components")
    @XmlElementRef
    private List<AbstractWidgetLocationComponent<?>> components;

    public void init(Pane pane) {
        for (LocationComponent component : components) {
            if (component instanceof WidgetLocationComponent widgetComponent) {
                widgetComponent.init(pane);
            }
        }
    }

    @Override
    public void add(LocationComponent locationComponent) {

    }

    @Override
    public void addAll(Iterable<? extends LocationComponent> locationComponents) {

    }

    @Override
    public CompletableFuture<Action> addAction(Action action) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> removeAction(Action action) {
        return null;
    }
}
