package com.kulygin.musiccloud;

import com.kulygin.musiccloud.config.ExternalConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicCloudApplication {
	public static void main(String[] args) {
		ExternalConfig.createUploadFolders();
		SpringApplication.run(MusicCloudApplication.class, args);
	}
}
