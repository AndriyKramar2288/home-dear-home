package org.banew.hdh.core.api;

import org.banew.hdh.core.api.components.Action;

import java.util.List;
import java.util.Map;

public interface Location {

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    List<LocationComponent> getComponents();

    List<Action> getActions();
}