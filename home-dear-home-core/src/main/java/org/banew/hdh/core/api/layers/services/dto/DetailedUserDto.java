package org.banew.hdh.core.api.layers.services.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DetailedUserDto(
        List<LocationDto> locations,
        String username,
        String fullname,
        String password,
        String email,
        String phoneNumber,
        String photoSrc,
        LocalDateTime lastTimeLogin
) {
}
