package org.banew.hdh.core.api.users;

import org.banew.hdh.core.api.Location;

import java.util.Set;

public interface User {
    String getUsername();
    String getFullname();
    String getPassword();
    String getEmail();
    String getPhoneNumber();
    Set<Location> getLocations();
}