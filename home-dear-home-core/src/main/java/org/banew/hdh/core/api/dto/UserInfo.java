package org.banew.hdh.core.api.dto;

import java.time.LocalDateTime;

public interface UserInfo extends DataPrototype<DetailedUserInfo> {
    String username();

    String fullname();

    String password();

    String email();

    String phoneNumber();

    String photoSrc();

    LocalDateTime lastTimeLogin();
}