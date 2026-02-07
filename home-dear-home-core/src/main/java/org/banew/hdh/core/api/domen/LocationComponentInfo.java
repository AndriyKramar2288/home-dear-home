package org.banew.hdh.core.api.domen;

import java.util.Map;

public interface LocationComponentInfo {
    String getId();

    String getFullClassName();

    String getName();

    void setName(String name);

    Map<String, String> getProperties();

    void setProperties(Map<String, String> properties);
}