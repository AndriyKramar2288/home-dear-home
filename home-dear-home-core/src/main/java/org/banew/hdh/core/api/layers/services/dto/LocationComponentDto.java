package org.banew.hdh.core.api.layers.services.dto;

import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.Map;

public record LocationComponentDto(
        String id,
        String fullClassName,
        LocationComponentAttributes classAttributes,
        String name,
        Map<String, String> properties
) {
}