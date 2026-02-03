package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlAttribute;
import org.banew.hdh.core.api.LocationComponent;

public abstract class XmlLocationComponent implements LocationComponent {
    @XmlAttribute
    private String name;

    @Override
    public String getName() {
        return name;
    }
}
