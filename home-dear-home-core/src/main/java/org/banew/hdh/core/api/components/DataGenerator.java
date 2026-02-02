package org.banew.hdh.core.api.components;

import java.util.Optional;
import java.util.function.Consumer;

public interface DataGenerator {
    Optional<String> whenGenerate(Consumer<Data> consumer, String ... args);
    boolean removeCallback(String callbackId);
}
