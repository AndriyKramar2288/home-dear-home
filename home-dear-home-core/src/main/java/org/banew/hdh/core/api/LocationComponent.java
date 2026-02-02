package org.banew.hdh.core.api;

import org.banew.hdh.core.api.components.DataGenerator;
import org.banew.hdh.core.api.components.DataProcessor;

public interface LocationComponent extends DataProcessor, DataGenerator {
    String getName();
}
