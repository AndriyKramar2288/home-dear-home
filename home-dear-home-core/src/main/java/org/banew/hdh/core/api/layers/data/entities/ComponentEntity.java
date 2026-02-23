package org.banew.hdh.core.api.layers.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class ComponentEntity {
    private String id;
    private String name;
    private Map<String, String> properties = new HashMap<>();
    private String fullClassName;
    private LocationComponentAttributes classAttributes;
}
