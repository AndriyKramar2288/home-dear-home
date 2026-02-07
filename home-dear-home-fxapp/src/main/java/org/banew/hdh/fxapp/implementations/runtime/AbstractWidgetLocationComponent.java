package org.banew.hdh.fxapp.implementations.runtime;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import org.banew.hdh.core.api.domen.LocationComponentInfo;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;
import org.banew.hdh.core.api.runtime.components.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractWidgetLocationComponent<T extends Parent> implements DesktopLocationComponent {

    private final Map<String, EventHandler<? extends Event>> actions = new HashMap<>();
    private LocationComponentInfo info;

    protected T widget;

    @Override
    public LocationComponentInfo getInfo() {
        return info;
    }

    @Override
    public void setInfo(LocationComponentInfo info) {
        this.info = info;
    }

    @Override
    public Optional<String> whenGenerate(Consumer<Data> consumer, Map<String, String> args) {

        validateArguments(args, getMetadata().generationArguments(), getMetadata().supportGeneration());

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
    public void process(Data data, Map<String, String> args) {
        validateArguments(args, getMetadata().processingArguments(), getMetadata().supportProcessing());
        processEvent(data, args);
    }

    protected abstract LocationComponentAttributes getMetadata();
    protected abstract EventHandler<? extends Event> whenGenerateEvent(Consumer<Data> consumer, Map<String, String> args);
    protected abstract void processEvent(Data data, Map<String, String> args);

    private void validateArguments(Map<String, String> args,
                                   LocationComponentAttributes.Argument[] arguments,
                                   boolean isSupports) {

        if (!isSupports && arguments.length != 0) {
            throw new IllegalArgumentException("Illegal metadata state: "
                    + "arguments length must be 0 when operation is not supported.");
        }

        for (LocationComponentAttributes.Argument argument : arguments) {
            if (argument.required()) {
                if (argument.name() == null) {
                    throw new IllegalArgumentException("Name of the argument cannot be null!");
                }
                if (args.get(argument.name()) == null) {
                    throw new IllegalArgumentException("Missing required argument " + argument.name());
                }
                if (!argument.dependsOn().isBlank() && args.get(argument.dependsOn()) == null) {
                    throw new IllegalArgumentException("Missing required depending argument " + argument.dependsOn());
                }
            }
        }
    }
}
