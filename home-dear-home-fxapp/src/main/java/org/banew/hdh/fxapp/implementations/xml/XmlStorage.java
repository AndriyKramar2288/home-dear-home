package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "storage")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlStorage {
    @XmlElementWrapper(name = "users", required = true)
    @XmlElement(name = "user")
    private List<XmlUserInfo> users = new ArrayList<>();
}