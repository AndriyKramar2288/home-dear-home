package org.banew.hdh.core.api.runtime;

import org.banew.hdh.core.api.layers.services.dto.LocationComponentDto;
import org.banew.hdh.core.api.layers.services.dto.LocationDto;
import org.banew.hdh.core.api.runtime.components.DataGenerator;
import org.banew.hdh.core.api.runtime.components.DataProcessor;

/**
 * Basic abstraction of the all possible components in the terms of {@link LocationDto} abstraction.
 * @param <C> Context class, that uses for lifecycle operations, as initialization
 */
public interface LocationComponent<C> extends DataProcessor, DataGenerator {
    void init(C context);
    LocationComponentDto getInfo();
    void setInfo(LocationComponentDto info);
}