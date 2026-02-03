package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.LocationComponent;
import org.banew.hdh.core.api.components.Action;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class XmlLocation implements Location {
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "name")
    private String name;
    @XmlElement(name = "description")
    private String description;
    @XmlElementWrapper(name = "components")
    @XmlElementRef
    private List<XmlLocationComponent> components;
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<XmlAction> actions;

    public List<LocationComponent> getComponents() {
        return (List<LocationComponent>) (List<?>) components;
    }

    public List<Action> getActions() {
        return (List<Action>) (List<?>) actions;
    }
}
