package org.banew.hdh.core.api.layers.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LocationEntity {
    private String id;
    private String name;
    private String description;
    private List<ComponentEntity> components = new ArrayList<>();
    private List<ActionEntity> actions = new ArrayList<>();
    private UserEntity owner;
}
