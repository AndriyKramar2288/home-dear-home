package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.dto.DetailedUserInfo;
import org.banew.hdh.core.api.dto.LocationInfo;
import org.banew.hdh.fxapp.implementations.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class XmlUserInfo implements DetailedUserInfo {
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
    public String username() {
        return username;
    }

    @Override
    public String fullname() {
        return fullname;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String phoneNumber() {
        return phoneNumber;
    }

    @Override
    public Set<LocationInfo> locations() {
        return new HashSet<>(locations);
    }

    @Override
    public LocalDateTime lastTimeLogin() {
        return lastTimeLogin;
    }

    @Override
    public DetailedUserInfo copy() {
        return toBuilder()
                .locations(new ArrayList<>(locations))
                .build();
    }
}
