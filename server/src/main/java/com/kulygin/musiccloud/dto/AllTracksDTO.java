package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Track;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

public class AllTracksDTO {

    Set<TrackDTO> tracks;

    public AllTracksDTO() {
    }

    public AllTracksDTO(Collection<Track> dbModel) {
        if (dbModel == null) {
            return;
        }
        this.tracks = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(TrackDTO::new)
                .collect(Collectors.toSet());
    }

    public Set<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(Set<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
