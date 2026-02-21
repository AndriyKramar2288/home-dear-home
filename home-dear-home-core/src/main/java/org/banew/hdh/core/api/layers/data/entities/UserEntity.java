package org.banew.hdh.core.api.layers.data.entities;

import java.time.LocalDateTime;
import java.util.List;

public interface UserEntity {
    String getUsername();

    String getPassword();

    String getEmail();

    String getFullname();

    String getPhoneNumber();

    String getPhotoSrc();

    LocalDateTime getLastTimeLogin();

    List<LocationEntity> getLocations();

    void setUsername(String username);

    void setPassword(String password);

    void setEmail(String email);

    void setFullname(String fullname);

    void setPhoneNumber(String phoneNumber);

    void setPhotoSrc(String photoSrc);

    void setLastTimeLogin(LocalDateTime lastTimeLogin);

    void setLocations(List<? extends LocationEntity> locations);
}
