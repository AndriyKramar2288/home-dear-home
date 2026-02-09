package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;
import org.banew.hdh.core.api.dto.ActionInfo;
import org.banew.hdh.core.api.dto.LocationComponentInfo;
import org.banew.hdh.core.api.dto.LocationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }

    @SuppressWarnings("unchecked")
    public List<LocationComponentInfo> components() {
        return (List<LocationComponentInfo>) (List<?>) components;
    }

    @SuppressWarnings("unchecked")
    public List<ActionInfo> actions() {
        return (List<ActionInfo>) (List<?>) actions;
    }

    @Override
    public XmlLocation copy() {
        return toBuilder()
                .components(components.stream()
                        .map(XmlLocationComponent::copy)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .actions(actions.stream()
                        .map(XmlAction::copy)
                        .collect(Collectors.toCollection(ArrayList::new)))
                .build();
    }
}