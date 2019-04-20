package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.service.StorageService;
import lombok.extern.log4j.Log4j;
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
@Log4j
public class StorageServiceImpl implements StorageService {
    private final Path rootLocationAudio = Paths.get("storage-audio");
    private final Path rootLocationPhoto = Paths.get("storage-photo");

    public void storeAudio(MultipartFile file, String fileName) {
        storeFile(file, fileName, this.rootLocationAudio);
    }

    public Resource loadAudioFile(String filename) {
        return getResource(filename, rootLocationAudio);
    }

    public void deleteAllAudio() {
        FileSystemUtils.deleteRecursively(rootLocationAudio.toFile());
    }

    public void initAudio() {
        try {
            Files.createDirectory(rootLocationAudio);
        } catch (IOException e) {
            log.error("Could not initialize storage!");
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    @Override
    public void storePhoto(MultipartFile file, String fileName) {
        storeFile(file, fileName, this.rootLocationPhoto);
    }

    public void storeFile(MultipartFile file, String fileName, Path rootLocationPhoto) {
        try {
            if (!Files.exists(rootLocationPhoto)) {
                Files.createDirectory(rootLocationPhoto);
            }
            Files.copy(file.getInputStream(), rootLocationPhoto.resolve(fileName));
        } catch (Exception e) {
            log.error("Error via file saving: ", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Resource loadPhotoFile(String filename) {
        return getResource(filename, rootLocationPhoto);
    }

    private Resource getResource(String filename, Path rootLocationPhoto) {
        try {
            Path file = rootLocationPhoto.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                log.error("Problems with resource reading");
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            log.error("Error via file loading");
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
            log.error("Could not initialize storage!");
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
