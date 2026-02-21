package org.banew.hdh.core.api.layers.data.entities;

import java.util.Map;

public interface ActionEntity {
    String getActionId();
    void setActionId(String actionId);

    String getSourceComponentName();
    void setTargetComponentName(String targetComponentName);

    Map<String, String> getSourceArgs();
    void setSourceArgs(Map<String, String> sourceArgs);

    String getTargetComponentName();
    void setSourceComponentName(String sourceComponentName);

    Map<String, String> getTargetArgs();
    void setTargetArgs(Map<String, String> targetArgs);
}