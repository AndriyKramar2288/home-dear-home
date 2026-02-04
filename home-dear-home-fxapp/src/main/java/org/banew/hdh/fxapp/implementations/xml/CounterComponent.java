package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.components.Data;
import org.banew.hdh.core.api.components.StringData;
import org.banew.hdh.fxapp.implementations.ComponentsContext;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@XmlRootElement(name = "counter")
public class CounterComponent extends AbstractWidgetLocationComponent<Label> {

    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void init(ComponentsContext componentsContext) {
        widget = new Label("hello");
        componentsContext.getMainPane().getChildren().add(widget);
    }

    @Override
    protected int getProcessArgsCount() {
        return 0;
    }

    @Override
    protected int getGenerateArgsCount() {
        return -1;
    }

    @Override
    protected EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, String... args) {
        return null;
    }

    @Override
    protected void processEvent(StringData stringData, String... args) {
        widget.setText("Number: " + counter.incrementAndGet());
    }
}
