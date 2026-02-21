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
public class XmlLocation implements LocationEntity {
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

    @Override
    public void setComponents(List<? extends ComponentEntity> comp) {
        for (ComponentEntity componentEntity : comp) {
            components.add((XmlLocationComponent) comp);
        }
    }

    @Override
    public void setActions(List<? extends ActionEntity> act) {
        for (ActionEntity actionEntity : act) {
            actions.add((XmlAction) actionEntity);
        }
    }
}