package org.banew.hdh.core.api.layers.services.dto;

import java.time.LocalDateTime;

public record UserDto(
        String username,
        String fullname,
        String password,
        String email,
        String phoneNumber,
        String photoSrc,
        LocalDateTime lastTimeLogin
) {
}