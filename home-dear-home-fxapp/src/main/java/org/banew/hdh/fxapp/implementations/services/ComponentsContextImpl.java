package org.banew.hdh.fxapp.implementations.services;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import org.banew.hdh.core.api.layers.components.ComponentsContextSource;
import org.banew.hdh.fxapp.ui.JavaFXApp;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ComponentsContextImpl implements ComponentsContextSource<ComponentsContextImpl> {

    private final JavaFXApp javaFXApp;

    public Pane getMainPane() {
        try {
            Node node = javaFXApp.getScene().getRoot().lookup("#pidaras");
            if (node == null) {
                throw new NullPointerException("There is no root node for pidaras");
            }
            return (Pane) node;
        }
        catch (ClassCastException | NullPointerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public ComponentsContextImpl get() {
        return this;
    }
}
