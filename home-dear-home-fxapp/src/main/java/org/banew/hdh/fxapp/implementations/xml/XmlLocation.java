package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.banew.hdh.core.api.domen.ActionInfo;
import org.banew.hdh.core.api.domen.LocationComponentInfo;
import org.banew.hdh.core.api.domen.LocationInfo;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class XmlLocation implements LocationInfo {
    @XmlAttribute(name = "id", required = true)
    @XmlID
    private String id;
    @XmlAttribute(name = "name", required = true)
    private String name;
    @XmlElement(name = "description")
    private String description;
    @XmlElementWrapper(name = "components", required = true)
    @XmlElementRef
    private List<XmlLocationComponent> components = new ArrayList<>();
    @XmlElementWrapper(name = "actions", required = true)
    @XmlElement(name = "action")
    private List<XmlAction> actions = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public List<LocationComponentInfo> getComponents() {
        return (List<LocationComponentInfo>) (List<?>)  components;
    }

    @SuppressWarnings("unchecked")
    public List<ActionInfo> getActions() {
        return (List<ActionInfo>) (List<?>)  actions;
    }
}