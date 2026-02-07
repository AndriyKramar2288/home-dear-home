package org.banew.hdh.fxapp.implementations.runtime;

import lombok.Data;

import java.util.Set;

@Data
public class Location {
    private final Set<DesktopLocationComponent> locationComponents;
}
