package org.banew.hdh.core.api.layers.components;

import org.banew.hdh.core.api.runtime.LocationComponent;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.Set;

public interface ComponentsClassesManager {
    Set<LocationComponentAttributes> getAvailableComponents();
    Class<? extends LocationComponent<?>> getLocationComponentClass(String classFullname);
}
