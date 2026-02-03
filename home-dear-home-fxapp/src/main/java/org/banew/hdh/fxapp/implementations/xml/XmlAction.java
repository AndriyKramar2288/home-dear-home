package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.components.Action;

@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAction implements Action {

    @XmlIDREF
    @XmlAttribute
    private XmlLocationComponent sourceComponent;

    @XmlElementWrapper(name = "source-args")
    @XmlElement(name = "source-arg")
    private String[] sourceArgs;

    @XmlIDREF
    @XmlAttribute
    private XmlLocationComponent targetComponent;

    @XmlElementWrapper(name = "target-args")
    @XmlElement(name = "target-arg")
    private String[] targetArgs;

    @Override
    public String sourceComponentName() {
        return sourceComponent.getName();
    }

    @Override
    public String[] sourceArgs() {
        return sourceArgs;
    }

    @Override
    public String targetComponentName() {
        return targetComponent.getName();
    }

    @Override
    public String[] targetArgs() {
        return targetArgs;
    }
}
