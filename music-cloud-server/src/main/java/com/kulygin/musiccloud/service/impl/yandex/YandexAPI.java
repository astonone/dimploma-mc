package com.kulygin.musiccloud.service.impl.yandex;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.exceptions.ServerIOException;
import com.yandex.disk.rest.json.Link;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

@Service
@Log4j
public class YandexAPI {
    private RestClient restClient;
    private Credentials credentials;

    @Value("${storage.user}")
    private String user;

    @Value("${storage.token}")
    private String token;

    @PostConstruct
    public void init() {
        credentials = new Credentials(user, token);
        restClient = new RestClient(credentials);
    }

    public File uploadFileToYandexDisk(MultipartFile uploadedFileRef, boolean isPicture) throws IOException, ServerException {
        String serverPathAudio = "audio-storage/";
        String serverPathPhoto = "photo-storage/";
        String serverPath = isPicture ? serverPathPhoto : serverPathAudio;

        Link uploadLink = restClient.getUploadLink(serverPath + uploadedFileRef.getOriginalFilename(), true);

        File file =  multipartToFile(uploadedFileRef);

        restClient.uploadFile(uploadLink, true, file, null);

        return file;
    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File(multipart.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multipart.getBytes());
        fos.close();
        return convFile;
    }

    public Resource loadAudioFileFromYandexDisk(String filename) {
        File file = new File("storage-audio/" + filename);
        try {
            file.createNewFile();
            com.yandex.disk.rest.json.Resource resource = restClient.getResources(new ResourcesArgs.Builder()
                    .setPath("/audio-storage/" + filename)
                    .setLimit(1)
                    .setOffset(2)
                    .build());

            restClient.downloadFile(resource.getPath().getPath(), file, null);
        } catch (Exception e) {
            log.error("Problems with file downloading: ", e);
        }
        try {
            UrlResource urlResource = new UrlResource(file.toPath().toUri());
            return urlResource;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public Resource loadPhotoFileFromYandexDisk(String filename) {
        File file = new File("storage-photo/" + filename);
        try {
            file.createNewFile();
            com.yandex.disk.rest.json.Resource resource = restClient.getResources(new ResourcesArgs.Builder()
                    .setPath("/photo-storage/" + filename)
                    .setLimit(1)
                    .setOffset(2)
                    .build());

            restClient.downloadFile(resource.getPath().getPath(), file, null);
        } catch (Exception e) {
            log.error("Problems with file downloading: ", e);
        }
        try {
            UrlResource urlResource = new UrlResource(file.toPath().toUri());
            return urlResource;
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
