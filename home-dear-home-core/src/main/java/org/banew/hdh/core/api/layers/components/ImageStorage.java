package org.banew.hdh.core.api.layers.components;

import java.net.URL;

public interface ImageStorage {
    URL saveAvatarImage(byte[] image, String fileName);
}
