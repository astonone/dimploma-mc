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
public class AllPlaylistsDTO {

    private Set<PlaylistDTO> playlists;

    public AllPlaylistsDTO() {
    }

    public AllPlaylistsDTO(Set<Playlist> dbModel) {

        if (dbModel == null) {
            return;
        }
        this.playlists = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(PlaylistDTO::new)
                .collect(Collectors.toSet());
    }
}
