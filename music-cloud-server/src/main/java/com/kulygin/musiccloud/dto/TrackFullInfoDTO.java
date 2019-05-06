package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.domain.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
public class TrackFullInfoDTO {

    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer year;
    private String filename;
    private String duration;
    private Double rating;
    private Set<GenreDTO> genres;
    private Set<PlaylistDTO> playlists;
    private Set<MoodDTO> moods;

    public TrackFullInfoDTO() {
    }

    public TrackFullInfoDTO(Track dbModel) {
        if (dbModel == null) {
            return;
        }

        this.id = dbModel.getId();
        this.title = dbModel.getTitle();
        this.artist = dbModel.getArtist();
        this.album = dbModel.getAlbum();
        this.year = dbModel.getYear();
        this.filename = dbModel.getFilename();
        this.duration = dbModel.getDuration();
        this.rating = dbModel.getRating();

        this.genres = ofNullable(dbModel.getGenres()).orElse(newHashSet()).stream()
                .map(GenreDTO::new)
                .collect(Collectors.toSet());

        this.playlists = ofNullable(dbModel.getPlaylists()).orElse(newHashSet()).stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toSet());

        this.moods = ofNullable(dbModel.getMoods()).orElse(newHashSet()).stream()
                .map(MoodDTO::new)
                .collect(Collectors.toSet());
    }
}
