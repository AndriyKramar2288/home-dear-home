package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import org.banew.hdh.core.api.Location;
import org.banew.hdh.core.api.users.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlUser implements User {
    @XmlAttribute
    private String username;
    @XmlAttribute
    private String password;
    @XmlAttribute
    private String email;
    @XmlAttribute
    private String fullname;
    @XmlAttribute
    private String phoneNumber;
    @XmlElementWrapper(name = "locations")
    @XmlElement(name = "location")
    private List<XmlLocation> locations;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getFullname() {
        return fullname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Set<Location> getLocations() {
        return new HashSet<>(locations);
    }
}
