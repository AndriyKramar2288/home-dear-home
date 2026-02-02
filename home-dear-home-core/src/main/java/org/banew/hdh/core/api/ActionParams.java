package org.banew.hdh.core.api;

public record ActionParams(
        String sourceComponentName,
        String[] sourceArgs,
        String targetComponentName,
        String[] targetArgs
) { }