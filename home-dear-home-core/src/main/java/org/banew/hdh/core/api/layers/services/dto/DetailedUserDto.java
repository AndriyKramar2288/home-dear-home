package org.banew.hdh.core.api.layers.services.dto;

import java.util.Set;

public record DetailedUserDto(
        Set<LocationDto> locations,
        UserDto userDto // TODO 何??!
) {
}
