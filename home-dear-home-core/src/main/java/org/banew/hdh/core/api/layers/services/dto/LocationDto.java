package org.banew.hdh.core.api.layers.services.dto;

import java.util.List;

public record LocationDto(
        String id,
        String name,
        String description,
        List<LocationComponentDto> components,
        List<ActionDto> actions
) {
}