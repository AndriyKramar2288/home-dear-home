package org.banew.hdh.core.api.layers.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class ActionEntity {
    private String actionId;
    private String sourceComponentName;
    private Map<String, String> sourceArgs = new HashMap<>();
    private String targetComponentName;
    private Map<String, String> targetArgs = new HashMap<>();
}