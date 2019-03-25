package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaylistDTO {

    private Long id;
    private String name;
    private DateDTO dateCreate;

    public PlaylistDTO() {
    }

    public PlaylistDTO(Playlist dbModel) {

        if (dbModel == null) {
            return;
        }

        this.id = dbModel.getId();
        this.name = dbModel.getName();
    }
}
