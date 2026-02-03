package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Action;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLocation implements Location {

    @XmlElementWrapper(name = "components")
    @XmlElementRef
    private List<XmlLocationComponent> components;
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<XmlAction> actions;

    public void init(Pane pane) {
        for (LocationComponent component : components) {
            if (component instanceof AbstractWidgetLocationComponent<?> widgetComponent) {
                widgetComponent.init(pane);
            }
        }
        if (actions != null) for (XmlAction action : actions) {
            var source = findByComponentName(action.sourceComponentName());
            var target = findByComponentName(action.targetComponentName());
            if (source != null && target != null) {
                source.whenGenerate(data -> {
                    data.loadInto(target, action.targetArgs());
                }, action.sourceArgs());
            }
        }
    }

    private LocationComponent findByComponentName(String componentName) {
        return components.stream()
                .filter(component -> component.getName().equals(componentName))
                .findFirst()
                .orElse(null);
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
