package org.banew.hdh.core.api;

import java.util.Optional;
import java.util.function.Consumer;

public interface DataGenerator {
    Optional<Long> whenGenerate(Consumer<Data> consumer, String ... args);
    boolean removeCallback(long callbackId);
}
