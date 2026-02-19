package org.banew.hdh.core.api.dto;

import java.util.Map;

public interface LocationComponentInfo extends DataPrototype<LocationComponentInfo> {
    String id();

    String fullClassName();

    String name();

    boolean isDefault();

    Map<String, String> properties();
}