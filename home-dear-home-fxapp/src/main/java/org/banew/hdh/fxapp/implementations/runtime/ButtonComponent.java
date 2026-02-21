package org.banew.hdh.fxapp.implementations.runtime;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.core.api.runtime.components.Data;
import org.banew.hdh.fxapp.implementations.services.ComponentsContextImpl;

import java.util.Map;
import java.util.function.Consumer;

@XmlRootElement(name = "button")
@XmlAccessorType(XmlAccessType.FIELD)
@LocationComponentAttributes(
        name = "Button",
        description = "Aboba",
        supportGeneration = true,
        generationArguments = {
                @LocationComponentAttributes.Argument(
                        name = "ababwewq",
                        required = true,
                        desc = "wqeeqeqwqwe",
                        format = "WWWWWW"
                ),
                @LocationComponentAttributes.Argument(
                        name = "qwwww",
                        desc = "sssssssssss",
                        format = "ZZZZ"
                )
        },
        properties = {
                @LocationComponentAttributes.Argument(
                        name = "AAAAAAA",
                        desc = "wwwww",
                        format = "ZZZZ"
                )
        }
)
public class ButtonComponent extends AbstractWidgetLocationComponent<Button> {

    @XmlAttribute
    private String text;
    @XmlAttribute
    private int x;
    @XmlAttribute
    private int y;

    @Override
    protected void processEvent(Data data, Map<String, String> args) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected LocationComponentAttributes getMetadata() {
        return getClass().getAnnotation(LocationComponentAttributes.class);
    }

    @Override
    protected EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, Map<String, String> args) {
        EventHandler<ActionEvent> handler = event -> {
            consumer.accept(new Data("Button clicked!"));
        };
        widget.addEventHandler(ActionEvent.ACTION, handler);
        return handler;
    }

    @Override
    public void init(ComponentsContextImpl componentsContextImpl) {
        widget = new Button(text);
        widget.setTranslateX(x);
        widget.setTranslateY(y);
        componentsContextImpl.getMainPane().getChildren().add(widget);
    }
}
