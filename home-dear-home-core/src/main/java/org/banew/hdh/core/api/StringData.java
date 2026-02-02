package org.banew.hdh.core.api;

public class StringData implements Data {
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public void loadInto(DataProcessor dataProcessor, String... args) {
        dataProcessor.process(this, args);
    }
}
