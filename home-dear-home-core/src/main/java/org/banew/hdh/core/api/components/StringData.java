package org.banew.hdh.core.api.components;

public class StringData implements Data {
    private final String string;

    public StringData(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    public void loadInto(DataProcessor dataProcessor, String... args) {
        dataProcessor.process(this, args);
    }
}
