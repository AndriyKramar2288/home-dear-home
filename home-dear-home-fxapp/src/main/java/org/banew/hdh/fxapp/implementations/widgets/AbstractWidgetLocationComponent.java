package org.banew.hdh.fxapp.implementations.widgets;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Data;
import org.banew.hdh.core.api.components.StringData;
import org.banew.hdh.fxapp.abstractions.WidgetLocationComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractWidgetLocationComponent<T extends Parent> implements WidgetLocationComponent {

    private final Map<String, EventHandler<? extends Event>> actions = new HashMap<>();

    protected String name;
    protected T widget;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Optional<String> whenGenerate(Consumer<Data> consumer, String... args) {
        validateArgsCount(getGenerateArgsCount(), args.length);

        EventHandler<? extends Event> handler = whenGenerateEvent(consumer, args);

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
        validateArgsCount(getProcessArgsCount(), args.length);
        processEvent(stringData, args);
    }

    protected abstract int getProcessArgsCount();
    protected abstract int getGenerateArgsCount();
    protected abstract EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, String... args);
    protected abstract void processEvent(StringData stringData, String... args);

    private void validateArgsCount(int expected, int actual) {
        if (actual == -1) {
            throw new IllegalArgumentException("You can't use this class with this purpose!");
        }

        if (expected < actual) {
            throw new IllegalArgumentException(
                    String.format("Wrong count of the arguments! Expected %d, actual %d", expected, actual));
        }
    }
}
