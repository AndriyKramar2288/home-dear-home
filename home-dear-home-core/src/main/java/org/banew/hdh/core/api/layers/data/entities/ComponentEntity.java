package org.banew.hdh.core.api.layers.data.entities;

import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.Map;

public interface ComponentEntity {
    String getId();

    String getName();

    Map<String, String> getProperties();

    String getFullClassName();

    LocationComponentAttributes getClassAttributes();

    void setId(String id);

    void setName(String name);

    void setProperties(Map<String, String> properties);

    void setFullClassName(String fullClassName);

    void setClassAttributes(LocationComponentAttributes classAttributes);
}
