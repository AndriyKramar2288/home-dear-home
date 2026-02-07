package org.banew.hdh.core.api.runtime.components;

import java.time.LocalDateTime;

public class Data {

    private final String data;
    private final LocalDateTime createdAt;

    public Data(String data) {
        this.data = data;
        this.createdAt = LocalDateTime.now();
    }

    public String getData() {
        return data;
    }
}
