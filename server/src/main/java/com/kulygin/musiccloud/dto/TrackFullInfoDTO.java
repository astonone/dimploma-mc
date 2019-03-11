package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Track;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

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

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    public Set<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }

    public Set<MoodDTO> getMoods() {
        return moods;
    }

    public void setMoods(Set<MoodDTO> moods) {
        this.moods = moods;
    }
}
