package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import org.banew.hdh.core.api.layers.data.entities.ComponentEntity;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.ActionEntity;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XmlLocation {
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
    @XmlElementRef
    private XmlUserInfo owner;
}