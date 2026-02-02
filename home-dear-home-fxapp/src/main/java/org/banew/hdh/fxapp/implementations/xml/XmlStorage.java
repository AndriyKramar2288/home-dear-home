package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "storage")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlStorage {

    @XmlElement(name = "users")
    public ListWrap users = new ListWrap();

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ListWrap {
        @XmlElement(name = "user")
        public List<XmlStorageUser> users = new ArrayList<>();
    }
}
