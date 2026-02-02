package org.banew.hdh.core.api;

import java.util.List;

public record LocationComponentAttributes(
    String name,
    String fullClassName,
    String description,
    List<Argument> generationArguments,
    List<Argument> processingArguments
) {
    public record Argument (
        String name,
        String desc,
        String format
    ) {}
}
