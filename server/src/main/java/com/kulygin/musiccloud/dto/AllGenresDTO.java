package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Genre;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
public class AllGenresDTO {

    private Set<GenreDTO> genres;

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
}
