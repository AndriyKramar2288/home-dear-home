package org.banew.hdh.core.api.runtime.components;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface DataGenerator {
    Optional<String> whenGenerate(Consumer<Data> consumer, Map<String, String> args);
    boolean removeCallback(String callbackId);
}
