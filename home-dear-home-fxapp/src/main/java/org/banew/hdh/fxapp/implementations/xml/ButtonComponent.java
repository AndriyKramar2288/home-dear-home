package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.components.Data;
import org.banew.hdh.core.api.components.StringData;

import java.util.function.Consumer;

@XmlRootElement(name = "button")
@XmlAccessorType(XmlAccessType.FIELD)
public class ButtonComponent extends AbstractWidgetLocationComponent<Button> {

    @XmlAttribute
    private String text;
    @XmlAttribute
    private int x;
    @XmlAttribute
    private int y;

    @Override
    protected int getProcessArgsCount() {
        return -1;
    }

    @Override
    protected int getGenerateArgsCount() {
        return 0;
    }

    @Override
    protected void processEvent(StringData stringData, String... args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, String... args) {
        EventHandler<ActionEvent> handler = event -> {
            consumer.accept(new StringData("Button clicked!"));
        };
        widget.addEventHandler(ActionEvent.ACTION, handler);
        return handler;
    }

    @Override
    public void init(Pane pane) {
        widget = new Button(text);
        widget.setTranslateX(x);
        widget.setTranslateY(y);
        pane.getChildren().add(widget);
    }
}
