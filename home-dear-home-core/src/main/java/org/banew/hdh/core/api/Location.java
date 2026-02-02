package org.banew.hdh.core.api;

import java.util.concurrent.CompletableFuture;

public interface Location {
    void add(LocationComponent locationComponent);
    void addAll(Iterable<? extends LocationComponent> locationComponents);
    CompletableFuture<Action> addAction(ActionParams actionParams);
    CompletableFuture<Boolean> removeAction(Action action);
}