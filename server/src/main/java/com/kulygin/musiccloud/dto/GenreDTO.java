package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Genre;

public class GenreDTO {

    private Long id;
    private String name;

    public GenreDTO() {
    }

    public GenreDTO(Genre genre) {
        if (genre == null) {
            return;
        }
        this.id = genre.getId();
        this.name = genre.getName();
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
