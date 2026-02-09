package org.banew.hdh.core.api.dto;

import java.util.List;

public interface LocationInfo extends DataPrototype<LocationInfo> {

    String id();

    String name();

    String description();

    List<LocationComponentInfo> components();

    List<ActionInfo> actions();
}