package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.dto.AllGenresDTO;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.GenreDTO;
import com.kulygin.musiccloud.exception.GenreHasExistsException;
import com.kulygin.musiccloud.exception.GenreIsNotExistsException;
import com.kulygin.musiccloud.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getGenre(@PathVariable("id") Long genreId) {
        Genre genre = genreService.getGenreById(genreId);
        if (genre == null) {
            return getErrorResponseBody(ApplicationErrorTypes.GENRE_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(genre), HttpStatus.OK);
    }

    @RequestMapping(value = "/findAllPagination", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGenresPagination(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        Page<Genre> genres = genreService.getAllGenresPagination(PageRequest.of(page.intValue(), pageSize.intValue(), new Sort(Sort.Direction.ASC, "id")));
        List<Genre> resultListOfGenres = genres.getContent();
        return new ResponseEntity<>(convert(resultListOfGenres), HttpStatus.OK);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllGenresn() {
        List<Genre> genres = genreService.findAll();
        return new ResponseEntity<>(convert(genres), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGenre(@PathVariable("id") Long genreId) {
        try {
            genreService.deleteGenreById(genreId);
        } catch (GenreIsNotExistsException genreIsNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.GENRE_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createGenre(@RequestParam("name") String name) {
        Genre genre = null;
        try {
            genre = genreService.createGenre(name);
        } catch (GenreHasExistsException genreHasExists) {
            return getErrorResponseBody(ApplicationErrorTypes.GENRE_HAS_EXISTS);
        }
        return new ResponseEntity<>(convert(genre), HttpStatus.OK);
    }

    private GenreDTO convert(Genre dbModel) {
        return (dbModel == null) ? null : new GenreDTO(dbModel);
    }

    private AllGenresDTO convert(List<Genre> dbModel) {
        return (dbModel == null) ? null : new AllGenresDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
