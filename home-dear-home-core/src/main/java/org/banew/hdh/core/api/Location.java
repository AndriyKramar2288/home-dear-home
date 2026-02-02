package org.banew.hdh.core.api;

import org.banew.hdh.core.api.components.Action;

import java.util.concurrent.CompletableFuture;

public interface Location {
    void add(LocationComponent locationComponent);
    void addAll(Iterable<? extends LocationComponent> locationComponents);
    CompletableFuture<Action> addAction(Action action);
    CompletableFuture<Boolean> removeAction(Action action);
}