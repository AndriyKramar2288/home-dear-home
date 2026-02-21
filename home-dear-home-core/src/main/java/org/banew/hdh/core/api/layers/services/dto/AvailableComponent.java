package org.banew.hdh.core.api.layers.services.dto;

import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

public record AvailableComponent(
        String fullClassName,
        LocationComponentAttributes attributes
) {
}
