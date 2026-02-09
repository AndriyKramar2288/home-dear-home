package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.dto.LocationComponentInfo;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "component")
public class XmlLocationComponent implements LocationComponentInfo {
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

    @Override
    public XmlLocationComponent copy() {
        return toBuilder()
                .properties(new HashMap<>(properties))
                .build();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String fullClassName() {
        return fullClassName;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<String, String> properties() {
        return new HashMap<>(properties);
    }
}