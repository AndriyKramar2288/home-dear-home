package org.banew.hdh.core.api.domen;

import java.util.Map;

public interface ActionInfo {
    String sourceComponentName();
    Map<String, String> sourceArgs();
    String targetComponentName();
    Map<String, String> targetArgs();
}