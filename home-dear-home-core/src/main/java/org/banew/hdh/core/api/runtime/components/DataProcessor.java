package org.banew.hdh.core.api.runtime.components;

import java.util.Map;

public interface DataProcessor {
    void process(Data data, Map<String, String> args);
}