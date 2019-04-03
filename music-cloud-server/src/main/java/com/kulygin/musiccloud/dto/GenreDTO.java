package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
