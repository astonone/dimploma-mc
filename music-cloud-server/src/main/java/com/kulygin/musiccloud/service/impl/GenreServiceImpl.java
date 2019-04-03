package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.exception.GenreHasExistsException;
import com.kulygin.musiccloud.exception.GenreIsNotExistsException;
import com.kulygin.musiccloud.repository.GenreRepository;
import com.kulygin.musiccloud.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElse(null);
    }

    @Override
    public Genre getGenreByName(String name) {
        return genreRepository.findByName(name);
    }

    @Override
    public void deleteGenreById(Long id) throws GenreIsNotExistsException {
        if (!genreRepository.existsById(id)) {
            throw new GenreIsNotExistsException();
        }
        genreRepository.deleteById(id);
    }

    @Override
    public Genre createGenre(String name) throws GenreHasExistsException {
        Genre genre = genreRepository.findByName(name);
        if (genre != null) {
            throw new GenreHasExistsException();
        }
        return genreRepository.save(Genre.builder()
                .name(name)
                .build());
    }

    @Override
    public Page<Genre> getAllGenresPagination(PageRequest pageRequest) {
        Page<Genre> genres = genreRepository.findAll(pageRequest);
        return genres;
    }
}
