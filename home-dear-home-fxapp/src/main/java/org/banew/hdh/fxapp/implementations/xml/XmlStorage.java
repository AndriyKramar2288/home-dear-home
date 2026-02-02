package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import lombok.Data;
import org.banew.hdh.core.api.users.User;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "storage")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlStorage {
    @XmlElementWrapper(name = "users")
    @XmlElement(name = "user")
    private List<XmlUser> users = new ArrayList<>();
}