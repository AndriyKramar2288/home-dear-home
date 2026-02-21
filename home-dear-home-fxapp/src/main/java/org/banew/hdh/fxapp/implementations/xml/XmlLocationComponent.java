package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.runtime.LocationComponentAttributes;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "component")
public class XmlLocationComponent implements ComponentEntity {
    @XmlID
    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute(required = true)
    private String name;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private Map<String, String> properties = new HashMap<>();

    @XmlAttribute(required = true)
    private String fullClassName;

    @XmlTransient
    private LocationComponentAttributes classAttributes;
}