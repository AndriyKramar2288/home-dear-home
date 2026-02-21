package org.banew.hdh.core.api.layers.components;

import org.banew.hdh.core.api.layers.services.dto.AvailableComponent;
import org.banew.hdh.core.api.runtime.LocationComponent;

import java.util.Set;

public interface ComponentsClassesManager {
    Set<AvailableComponent> getAvailableComponents();
    Class<? extends LocationComponent<?>> getLocationComponentClass(String classFullname);
}
