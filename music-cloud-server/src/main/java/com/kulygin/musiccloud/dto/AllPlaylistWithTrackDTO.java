package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AllPlaylistWithTrackDTO {

    private List<PlaylistWithTrackDTO> playlistWithTrackDTOS;

    public AllPlaylistWithTrackDTO(List<Playlist> dbModel) {
        if (dbModel == null) {
            return;
        }

        this.playlistWithTrackDTOS = dbModel.stream()
                .map(PlaylistWithTrackDTO::new)
                .collect(Collectors.toList());
    }
}
