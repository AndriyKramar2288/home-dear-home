package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlID;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.fxapp.implementations.ComponentsContext;

import java.util.HashMap;
import java.util.Map;

public abstract class XmlLocationComponent implements LocationComponent<ComponentsContext> {
    @XmlID
    @XmlAttribute(required = true)
    private String name;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private Map<String, String> customProperties = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProperty(String key) {
        return customProperties.get(key);
    }

    @Override
    public void setProperty(String key, String value) {
        customProperties.put(key, value);
    }
}