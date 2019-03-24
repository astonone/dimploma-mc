package com.kulygin.musiccloud.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExternalConfig {
    public static void createUploadFolders() {
        try {
            Path path = Paths.get(System.getProperty("user.home") + "/.music-cloud/audio-storage/");
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            path = Paths.get(System.getProperty("user.home") + "/.music-cloud/photo-storage/");
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
