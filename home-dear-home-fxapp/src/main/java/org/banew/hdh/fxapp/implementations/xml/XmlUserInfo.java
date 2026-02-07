package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import org.banew.hdh.core.api.domen.LocationInfo;
import org.banew.hdh.core.api.domen.UserInfo;
import org.banew.hdh.fxapp.implementations.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class XmlUserInfo implements UserInfo {
    @XmlAttribute(required = true)
    private String username;
    @XmlAttribute(required = true)
    private String password;
    @XmlAttribute(required = true)
    private String email;
    @XmlAttribute
    private String fullname;
    @XmlAttribute
    private String phoneNumber;
    @XmlAttribute
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastTimeLogin;
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
    public Set<LocationInfo> getLocations() {
        return new HashSet<>(locations);
    }

    @Override
    public LocalDateTime getLastTimeLogin() {
        return lastTimeLogin;
    }

    @Override
    public void setLastTimeLogin(LocalDateTime lastTimeLogin) {
        this.lastTimeLogin = lastTimeLogin;
    }
}
