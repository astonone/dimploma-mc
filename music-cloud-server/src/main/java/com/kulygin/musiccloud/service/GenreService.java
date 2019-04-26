package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.exception.GenreHasExistsException;
import com.kulygin.musiccloud.exception.GenreIsNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

public interface GenreService {

    Genre getGenreById(Long id);

    Genre getGenreByName(String name);

    void deleteGenreById(Long id) throws GenreIsNotExistsException;

    Genre createGenre(String name) throws GenreHasExistsException;

    Page<Genre> getAllGenresPagination(PageRequest pageRequest);

    List<Genre> findAll();

    Set<Genre> findAllByIds(List<Long> ids);
}
