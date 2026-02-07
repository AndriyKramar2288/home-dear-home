package org.banew.hdh.fxapp.implementations.services;

import org.banew.hdh.core.api.services.UserService;
import org.banew.hdh.core.api.domen.UserInfo;
import org.banew.hdh.core.api.runtime.forms.LoginForm;
import org.banew.hdh.core.api.runtime.forms.RegisterForm;
import org.banew.hdh.fxapp.implementations.AuthorizationContext;
import org.banew.hdh.fxapp.implementations.StorageRepository;
import org.banew.hdh.fxapp.implementations.XmlService;
import org.banew.hdh.fxapp.implementations.xml.XmlUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorizationContext authorizationContext;

    @Override
    @Async
    public CompletableFuture<? extends UserInfo> register(RegisterForm registerForm) {
        try {
            registerForm.validate();

            XmlUserInfo xmlUser = new XmlUserInfo();
            xmlUser.setEmail(registerForm.email());
            xmlUser.setPassword(passwordEncoder.encode(registerForm.password()));
            xmlUser.setUsername(registerForm.username());
            storageRepository.saveUser(xmlUser);

            return CompletableFuture.completedFuture(xmlUser);
        }
        catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async
    public CompletableFuture<? extends UserInfo> login(LoginForm loginForm) {
        try {
            // Логіка пошуку та перевірки
            var user = storageRepository.findByUsername(loginForm.username())
                    .orElseThrow(() -> new IllegalArgumentException("Username or password is invalid"));

            if (!passwordEncoder.matches(loginForm.password(), user.getPassword())) {
                throw new IllegalArgumentException("Username or password is invalid");
            }

            authorizationContext.setCurrentUser(user);
            return CompletableFuture.completedFuture(user);

        } catch (Exception e) {
            // Явно кажемо: "Цей запуск завершився провалом"
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async
    public CompletableFuture<String> saveAvatarImage(byte[] image, String fileName) {
        try {
            // 1. Визначаємо шлях до папки (наприклад, "uploads/avatars")
            Path uploadPath = Paths.get(XmlService.USER_FILES_PREFIX, "uploads", "avatars");

            // 2. Створюємо папки, якщо їх ще немає
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 3. Генеруємо унікальне ім'я, зберігаючи розширення
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueName = UUID.randomUUID().toString() + extension;

            // 4. Повний шлях до файлу
            Path filePath = uploadPath.resolve(uniqueName);

            // 5. ЗАПИС БАЙТІВ (Магія NIO)
            Files.write(filePath, image);

            // Повертаємо рядок, який потім можна юзати як URI
            return CompletableFuture.completedFuture(filePath.toUri().toString());
        }
        catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    public UserInfo getCurrentUser() {
        return authorizationContext.getCurrentUser();
    }

    @Override
    public Optional<? extends UserInfo> availableLoginByPassword() {
        return storageRepository.findByLastTimeLoginAfterThan(LocalDateTime.now().minusDays(3));
    }

    @Override
    @Async
    public CompletableFuture<? extends UserInfo> login(String password) {
        UserInfo userInfo = availableLoginByPassword()
                .orElseThrow(() -> new IllegalArgumentException("You can't login this way now!"));

        if (passwordEncoder.matches(password, userInfo.getPassword())) {
            authorizationContext.setCurrentUser(userInfo);
            return CompletableFuture.completedFuture(userInfo);
        }
        else {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Wrong password!"));
        }
    }

    @Override
    public void logout() {
        authorizationContext.logout();
    }
}
