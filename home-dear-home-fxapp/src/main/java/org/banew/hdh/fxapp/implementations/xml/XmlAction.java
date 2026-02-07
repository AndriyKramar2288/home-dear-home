package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.domen.ActionInfo;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAction implements ActionInfo {

    @XmlIDREF
    @XmlAttribute(required = true)
    private XmlLocationComponent sourceComponent;

    @XmlElementWrapper(name = "source-args")
    @XmlElement(name = "source-arg")
    private Map<String, String> sourceArgs = new HashMap<>();

    @XmlIDREF
    @XmlAttribute(required = true)
    private XmlLocationComponent targetComponent;

    @XmlElementWrapper(name = "target-args")
    @XmlElement(name = "target-arg")
    private Map<String, String> targetArgs = new HashMap<>();

    @Override
    public String sourceComponentName() {
        return sourceComponent.getName();
    }

    @Override
    public Map<String, String> sourceArgs() {
        return sourceArgs;
    }

    @Override
    public String targetComponentName() {
        return targetComponent.getName();
    }

    @Override
    public Map<String, String> targetArgs() {
        return targetArgs;
    }
}
