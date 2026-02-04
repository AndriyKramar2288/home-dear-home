package org.banew.hdh.core.api;

import org.banew.hdh.core.api.components.DataGenerator;
import org.banew.hdh.core.api.components.DataProcessor;

/**
 * Basic abstraction of the all possible components in the terms of {@link Location} abstraction.
 * @param <C> Context class, that uses for lifecycle operations, as initialization
 */
public interface LocationComponent<C> extends DataProcessor, DataGenerator {
    void init(C context);
    String getName();
    String getProperty(String name);
    void setProperty(String name, String value);
}