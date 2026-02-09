package org.banew.hdh.core.api.dto;

import java.util.Set;

public interface DetailedUserInfo extends UserInfo {
    Set<LocationInfo> locations();
}
