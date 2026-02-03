package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlTransient;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.banew.hdh.core.api.components.Data;
import org.banew.hdh.core.api.components.StringData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractWidgetLocationComponent<T extends Parent> extends XmlLocationComponent {

    private final Map<String, EventHandler<? extends Event>> actions = new HashMap<>();
    protected T widget;

    @Override
    public Optional<String> whenGenerate(Consumer<Data> consumer, String... args) {
        validateArgsCount(getGenerateArgsCount(), args);

        EventHandler<? extends Event> handler = whenGenerateEvent(consumer, args);

        if (handler == null) {
            return Optional.empty();
        }

        var id = UUID.randomUUID().toString();
        actions.put(id, handler);

        return Optional.of(id);
    }

    @Override
    public boolean removeCallback(String callbackId) {
        return actions.remove(callbackId) != null;
    }

    @Override
    public void process(StringData stringData, String... args) {
        validateArgsCount(getProcessArgsCount(), args);
        processEvent(stringData, args);
    }

    public abstract void init(Pane pane);
    protected abstract int getProcessArgsCount();
    protected abstract int getGenerateArgsCount();
    protected abstract EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, String... args);
    protected abstract void processEvent(StringData stringData, String... args);

    private void validateArgsCount(int expected, String... args) {

        int actual = args == null ? 0 : args.length;

//        if (expected == -1 || actual != 0) {
//            throw new IllegalArgumentException("You can't use this class with this purpose!");
//        } TODO

        if (actual < expected) {
            throw new IllegalArgumentException(
                    String.format("Wrong count of the arguments! Expected %d, actual %d", expected, actual));
        }
    }
}
