package org.banew.hdh.core.api.dto;

import java.util.Map;

public interface ActionInfo extends DataPrototype<ActionInfo> {

    String id();

    String sourceComponentName();

    Map<String, String> sourceArgs();

    String targetComponentName();

    Map<String, String> targetArgs();
}