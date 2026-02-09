package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.dto.ActionInfo;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class XmlAction implements ActionInfo {

    @XmlAttribute(required = true)
    @XmlID
    private String actionId;

    @XmlAttribute(required = true)
    private String sourceComponentName;

    @XmlElementWrapper(name = "source-args")
    @XmlElement(name = "source-arg")
    private Map<String, String> sourceArgs = new HashMap<>();

    @XmlAttribute(required = true)
    private String targetComponentName;

    @XmlElementWrapper(name = "target-args")
    @XmlElement(name = "target-arg")
    private Map<String, String> targetArgs = new HashMap<>();

    @Override
    public String id() {
        return actionId;
    }

    @Override
    public String sourceComponentName() {
        return sourceComponentName;
    }

    @Override
    public Map<String, String> sourceArgs() {
        return new HashMap<>(sourceArgs);
    }

    @Override
    public String targetComponentName() {
        return targetComponentName;
    }

    @Override
    public Map<String, String> targetArgs() {
        return new HashMap<>(targetArgs);
    }

    @Override
    public XmlAction copy() {
        return toBuilder()
                .sourceArgs(new HashMap<>(sourceArgs))
                .targetArgs(new HashMap<>(targetArgs))
                .build();
    }
}
