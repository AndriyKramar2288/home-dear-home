package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.components.Action;

@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAction implements Action {
    @XmlAttribute
    private String sourceComponentName;
    @XmlElementWrapper(name = "source-args")
    @XmlElement(name = "source-arg")
    private String[] sourceArgs;
    @XmlAttribute
    private String targetComponentName;
    @XmlElementWrapper(name = "target-args")
    @XmlElement(name = "target-arg")
    private String[] targetArgs;

    @Override
    public String sourceComponentName() {
        return sourceComponentName;
    }

    @Override
    public String[] sourceArgs() {
        return sourceArgs;
    }

    @Override
    public String targetComponentName() {
        return targetComponentName;
    }

    @Override
    public String[] targetArgs() {
        return targetArgs;
    }
}
