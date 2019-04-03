package com.kulygin.musiccloud.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface StorageService {

    void storeAudio(MultipartFile file, String fileName);

    Resource loadAudioFile(String filename);

    void deleteAllAudio();

    void initAudio();

    void storePhoto(MultipartFile file, String fileName);

    Resource loadPhotoFile(String filename);

    void deleteAllPhoto();

    void initPhoto();
}
