package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
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
}
