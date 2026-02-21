package org.banew.hdh.core.api.layers.services;

import org.banew.hdh.core.api.layers.services.dto.DetailedUserDto;
import org.banew.hdh.core.api.layers.services.dto.LoginForm;
import org.banew.hdh.core.api.layers.services.dto.RegisterForm;
import org.banew.hdh.core.api.layers.services.dto.UserDto;

public interface UserService {

    DetailedUserDto register(RegisterForm registerForm);

    DetailedUserDto login(LoginForm loginForm);

    DetailedUserDto getCurrentUser();

    UserDto availableLoginByPassword();

    DetailedUserDto login(String password);

    void logout();
}