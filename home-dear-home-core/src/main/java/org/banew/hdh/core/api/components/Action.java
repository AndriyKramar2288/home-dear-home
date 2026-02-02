package org.banew.hdh.core.api.components;

public interface Action {
    String sourceComponentName();
    String[] sourceArgs();
    String targetComponentName();
    String[] targetArgs();
}