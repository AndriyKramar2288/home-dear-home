package org.banew.hdh.fxapp.implementations.xml;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.banew.hdh.core.api.layers.data.entities.LocationEntity;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.fxapp.implementations.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XmlUserInfo implements UserEntity {
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
    private String photoSrc;
    @XmlAttribute
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime lastTimeLogin;
    @XmlElementWrapper(name = "locations")
    @XmlElement(name = "location")
    private List<XmlLocation> locations = new ArrayList<>();

    @Override
    public List<LocationEntity> getLocations() {
        return locations.stream().map(e -> (LocationEntity) e).toList();
    }

    @Override
    public void setLocations(List<? extends LocationEntity> l) {
        for (LocationEntity locationEntity : l) {
            locations.add((XmlLocation) locationEntity);
        }
    }
}