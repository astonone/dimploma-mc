package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

public class PlaylistWithTrackDTO {

    private Long id;
    private String name;
    private Set<TrackDTO> tracks;

    public PlaylistWithTrackDTO() {
    }

    public PlaylistWithTrackDTO(Playlist dbModel) {

        if (dbModel == null) {
            return;
        }

        this.id = dbModel.getId();
        this.name = dbModel.getName();
        this.tracks = ofNullable(dbModel.getTracks()).orElse(newHashSet()).stream()
                .map(TrackDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(Set<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
