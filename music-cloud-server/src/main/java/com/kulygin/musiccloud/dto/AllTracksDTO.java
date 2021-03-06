package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Track;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
public class AllTracksDTO {

    private Set<TrackDTO> tracks;
    private int countAll;

    public AllTracksDTO() {
    }

    public AllTracksDTO(Collection<Track> dbModel, int count) {
        if (dbModel == null) {
            return;
        }
        this.tracks = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(TrackDTO::new)
                .collect(Collectors.toSet());
        this.countAll = count;
    }
}
