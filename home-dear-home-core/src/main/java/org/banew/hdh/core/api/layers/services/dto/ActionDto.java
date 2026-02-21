package org.banew.hdh.core.api.layers.services.dto;

import java.util.Map;

public record ActionDto(
        String id,
        String sourceComponentName,
        Map<String, String> sourceArgs,
        String targetComponentName,
        Map<String, String> targetArgs
) {
}