package org.banew.hdh.core.api.layers.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class UserEntity {
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String photoSrc;
    private LocalDateTime lastTimeLogin;
    private List<LocationEntity> locations = new ArrayList<>();
}
