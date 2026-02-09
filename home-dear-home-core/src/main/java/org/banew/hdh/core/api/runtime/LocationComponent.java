package org.banew.hdh.core.api.runtime;

import org.banew.hdh.core.api.dto.LocationComponentInfo;
import org.banew.hdh.core.api.runtime.components.DataGenerator;
import org.banew.hdh.core.api.runtime.components.DataProcessor;

/**
 * Basic abstraction of the all possible components in the terms of {@link org.banew.hdh.core.api.dto.LocationInfo} abstraction.
 * @param <C> Context class, that uses for lifecycle operations, as initialization
 */
public interface LocationComponent<C> extends DataProcessor, DataGenerator {
    void init(C context);
    LocationComponentInfo getInfo();
    void setInfo(LocationComponentInfo info);
}