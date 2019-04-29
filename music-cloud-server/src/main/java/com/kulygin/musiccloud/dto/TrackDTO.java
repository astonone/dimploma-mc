package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Track;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class TrackDTO {

    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer year;
    private String filename;
    private String duration;
    private Double rating;

    public TrackDTO() {
    }

    public TrackDTO(Track dbModel) {

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
    }
}
