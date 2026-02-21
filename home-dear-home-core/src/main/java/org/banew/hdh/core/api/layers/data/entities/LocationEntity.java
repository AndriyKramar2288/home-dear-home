package org.banew.hdh.core.api.layers.data.entities;

import java.util.List;

public interface LocationEntity {
    String getId();

    String getName();

    String getDescription();

    List<ComponentEntity> getComponents();

    List<ActionEntity> getActions();

    void setId(String id);

    void setName(String name);

    void setDescription(String description);

    void setComponents(List<? extends ComponentEntity> components);

    void setActions(List<? extends ActionEntity> actions);
}
