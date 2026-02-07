package org.banew.hdh.core.api.domen;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserInfo {
    String getUsername();
    String getFullname();
    String getPassword();
    String getEmail();
    String getPhoneNumber();
    LocalDateTime getLastTimeLogin();
    void setLastTimeLogin(LocalDateTime lastTimeLogin);
    Set<LocationInfo> getLocations();
}