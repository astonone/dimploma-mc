package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageServiceImpl implements StorageService {
    private final Path rootLocationAudio = Paths.get("storage-audio");
    private final Path rootLocationPhoto = Paths.get("storage-photo");

    public void storeAudio(MultipartFile file, String fileName) {
        try {
            if (!Files.exists(this.rootLocationAudio)) {
                Files.createDirectory(this.rootLocationAudio);
            }
            Files.copy(file.getInputStream(), this.rootLocationAudio.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Resource loadAudioFile(String filename) {
        try {
            Path file = rootLocationAudio.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    public void deleteAllAudio() {
        FileSystemUtils.deleteRecursively(rootLocationAudio.toFile());
    }

    public void initAudio() {
        try {
            Files.createDirectory(rootLocationAudio);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    @Override
    public void storePhoto(MultipartFile file, String fileName) {
        try {
            if (!Files.exists(this.rootLocationPhoto)) {
                Files.createDirectory(this.rootLocationPhoto);
            }
            Files.copy(file.getInputStream(), this.rootLocationPhoto.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Resource loadPhotoFile(String filename) {
        try {
            Path file = rootLocationPhoto.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Override
    public void deleteAllPhoto() {
        FileSystemUtils.deleteRecursively(rootLocationPhoto.toFile());
    }

    @Override
    public void initPhoto() {
        try {
            Files.createDirectory(rootLocationPhoto);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
