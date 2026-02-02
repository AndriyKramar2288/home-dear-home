package org.banew.hdh.fxapp.implementations;

import org.banew.hdh.core.api.Action;
import org.banew.hdh.core.api.ActionParams;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LocationImpl implements Location {

    private final Map<String, LocationComponent> components = new HashMap<>();
    private final Set<Action> actions = new HashSet<>();

    @Override
    public void add(LocationComponent locationComponent) {
        components.put( locationComponent.getName(), locationComponent );
    }

    @Override
    public void addAll(Iterable<? extends LocationComponent> locationComponents) {
        locationComponents.forEach(component -> {
            components.put(component.getName(), component);
        });
    }

    @Override
    public CompletableFuture<Action> addAction(ActionParams actionParams) {
        var sourceComponent = components.get(actionParams.sourceComponentName());
        var targetComponent = components.get(actionParams.targetComponentName());

        Action action = new Action(sourceComponent,
                targetComponent,
                actionParams.sourceArgs(),
                actionParams.targetArgs());

        action.bound();

        return CompletableFuture.completedFuture(action);
    }

    @Override
    public CompletableFuture<Boolean> removeAction(Action action) {
        return action.unbound();
    }
}
