package org.banew.hdh.core.api.domen;

import java.util.List;

public interface LocationInfo {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    List<LocationComponentInfo> getComponents();

    List<ActionInfo> getActions();
}