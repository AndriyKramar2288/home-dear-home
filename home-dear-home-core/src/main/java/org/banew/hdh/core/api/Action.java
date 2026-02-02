package org.banew.hdh.core.api;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Action {

    private boolean isBounded;

    private Long id;

    private final LocationComponent source;
    private final LocationComponent target;
    private final String[] sourceArgs;
    private final String[] targetArgs;

    public Action(LocationComponent source, LocationComponent target, String[] sourceArgs, String[] targetArgs) {
        this.source = source;
        this.target = target;
        this.sourceArgs = sourceArgs;
        this.targetArgs = targetArgs;
    }

    public boolean isBounded() {
        return isBounded;
    }

    public CompletableFuture<Boolean> bound() {
        Optional<Long> optionalId = source.whenGenerate(data -> data.loadInto(target, targetArgs), sourceArgs);
        isBounded = optionalId.isPresent();
        optionalId.ifPresent(id -> {
            this.id = id;
        });
        return CompletableFuture.completedFuture(isBounded);
    }

    public CompletableFuture<Boolean> unbound() {
        if (id != null && isBounded) {
            if (source.removeCallback(id)) {
                id = null;
                isBounded = false;
            }
        }

        return CompletableFuture.completedFuture(isBounded);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;
        return isBounded == action.isBounded
                && Objects.equals(id, action.id)
                && source.equals(action.source)
                && target.equals(action.target)
                && Arrays.equals(sourceArgs, action.sourceArgs)
                && Arrays.equals(targetArgs, action.targetArgs);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
