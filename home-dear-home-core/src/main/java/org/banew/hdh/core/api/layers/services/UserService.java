package org.banew.hdh.core.api.layers.services;

import org.banew.hdh.core.api.layers.services.dto.LoginForm;
import org.banew.hdh.core.api.layers.services.dto.RegisterForm;
import org.banew.hdh.core.api.layers.services.dto.UserDto;

public interface UserService {

    UserDto register(RegisterForm registerForm);

    UserDto login(LoginForm loginForm);

    UserDto getCurrentUser();

    UserDto availableLoginByPassword();

    UserDto login(String password);

    void logout();
}