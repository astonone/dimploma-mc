package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Playlist;

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
}
