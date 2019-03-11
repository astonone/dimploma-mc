package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Track;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
