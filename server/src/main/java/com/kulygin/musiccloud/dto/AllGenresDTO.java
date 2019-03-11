package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Genre;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

public class AllGenresDTO {

    Set<GenreDTO> genres;

    public AllGenresDTO() {
    }

    public AllGenresDTO(Collection<Genre> dbModel) {

        if (dbModel == null) {
            return;
        }

        this.genres = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(GenreDTO::new)
                .collect(Collectors.toSet());
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }
}
