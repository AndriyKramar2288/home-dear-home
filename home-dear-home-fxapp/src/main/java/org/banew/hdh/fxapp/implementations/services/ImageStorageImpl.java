package org.banew.hdh.fxapp.implementations.services;

import org.banew.hdh.core.api.layers.components.ImageStorage;
import org.banew.hdh.fxapp.implementations.XmlService;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageStorageImpl implements ImageStorage {
    @Override
    public URL saveAvatarImage(byte[] image, String fileName) {
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
            return filePath.toUri().toURL();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
