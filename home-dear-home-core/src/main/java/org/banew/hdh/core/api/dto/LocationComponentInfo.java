package org.banew.hdh.core.api.dto;

import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.Map;

public interface LocationComponentInfo extends DataPrototype<LocationComponentInfo> {
    String id();

    String fullClassName();

    LocationComponentAttributes classAttributes();

    String name();

    Map<String, String> properties();
}