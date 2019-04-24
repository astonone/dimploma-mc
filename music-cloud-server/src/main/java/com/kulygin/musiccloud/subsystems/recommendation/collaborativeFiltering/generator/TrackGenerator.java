package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator;

import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.service.TrackService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j
@Component
public class TrackGenerator {
    private List<String> artists;

    @Autowired
    private TrackService trackService;

    private void readData() {
        artists = new ArrayList<>();
        try {
            artists.addAll(Files.readAllLines(Paths.get("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\additional\\rcsData\\generator\\music\\music.txt")));
        } catch (IOException e) {
            log.error("Error via file reading: ",e);
        }
    }

    public List<Track> generateTracks(Integer trackCount) {
        readData();
        List<Track> tracks = new ArrayList<>();

        for (Integer i = 1; i <= trackCount; i++) {

            String artist = artists.get(GeneratorUtils.rnd(0, artists.size() - 1));
            String trackName = "Track" + GeneratorUtils.rnd(1,50);
            String album = "Album" + GeneratorUtils.rnd(1,3);

            Track track = Track.builder()
                    .id(i.longValue())
                    .artist(artist)
                    .title(artist + " - " + trackName)
                    .album(album)
                    .year(GeneratorUtils.rnd(2000,2018))
                    .duration(GeneratorUtils.rnd(120, 240) + "sec")
                    .filename(UUID.randomUUID().toString() + ".mp3")
                    .build();

            tracks.add(track);
        }

        return trackService.saveAll(tracks);
    }
}
