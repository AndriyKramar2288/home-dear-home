package org.banew.hdh.core.implementations;

import org.banew.hdh.core.api.layers.components.AuthorizationContext;
import org.banew.hdh.core.api.layers.components.BasicMapper;
import org.banew.hdh.core.api.layers.data.UserRepository;
import org.banew.hdh.core.api.layers.data.entities.UserEntity;
import org.banew.hdh.core.api.layers.services.UserService;
import org.banew.hdh.core.api.layers.services.dto.DetailedUserDto;
import org.banew.hdh.core.api.layers.services.dto.LoginForm;
import org.banew.hdh.core.api.layers.services.dto.RegisterForm;
import org.banew.hdh.core.api.layers.services.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationContext authorizationContext;
    private final BasicMapper basicMapper;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthorizationContext authorizationContext,
                           BasicMapper basicMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorizationContext = authorizationContext;
        this.basicMapper = basicMapper;
    }

    @Override
    public DetailedUserDto register(RegisterForm registerForm) {
        registerForm.validate();

        UserEntity user = userRepository.create();
        user.setEmail(registerForm.email());
        user.setPhotoSrc(registerForm.photoSrc());
        user.setPassword(passwordEncoder.encode(registerForm.password()));
        user.setUsername(registerForm.username());
        userRepository.save(user);
        authorizationContext.setCurrentUser(user);

        return basicMapper.userEntityToDetailedDto(user);
    }

    @Override
    public DetailedUserDto login(LoginForm loginForm) {
        // Логіка пошуку та перевірки
        var user = userRepository.findByUsername(loginForm.username())
                .orElseThrow(() -> new IllegalArgumentException("Username or password is invalid"));

        if (!passwordEncoder.matches(loginForm.password(), user.getPassword())) {
            throw new IllegalArgumentException("Username or password is invalid");
        }

        authorizationContext.setCurrentUser(user);
        return basicMapper.userEntityToDetailedDto(user);
    }

    @Override
    public DetailedUserDto getCurrentUser() {
        return authorizationContext.getCurrentUser() == null ? null
                : basicMapper.userEntityToDetailedDto(authorizationContext.getCurrentUser());
    }

    @Override
    public UserDto availableLoginByPassword() {
        return basicMapper.userEntityToDto(
                userRepository.findByLastTimeLoginAfterThan(LocalDateTime.now().minusDays(3)).orElseThrow());
    }

    @Override
    public DetailedUserDto login(String password) {
        UserEntity userInfo = userRepository
                .findByLastTimeLoginAfterThan(LocalDateTime.now().minusDays(3))
                .orElseThrow();

        if (passwordEncoder.matches(password, userInfo.getPassword())) {
            authorizationContext.setCurrentUser(userInfo);
            return basicMapper.userEntityToDetailedDto(userInfo);
        }
        else {
            throw new IllegalArgumentException("Wrong password!");
        }
    }

    @Override
    public void logout() {
        authorizationContext.setCurrentUser(null);
    }
}
