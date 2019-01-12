package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

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

    public Set<PlaylistDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<PlaylistDTO> playlists) {
        this.playlists = playlists;
    }
}
