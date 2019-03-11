package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.*;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.sun.media.sound.InvalidDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public interface TrackService {

    Track getTrackById(Long id);

    void deleteTrackById(Long id) throws TrackIsNotExistsException;

    Track createTrack(String filename) throws TrackHasExistsException, InvalidDataException, IOException, UnsupportedTagException, FileIsNotExistsException, PlaylistNotExistsException, TrackIsNotExistsException, com.mpatric.mp3agic.InvalidDataException;

    Track updateTrack(Long trackId, String title, String artist, String album, Integer year, String filename, String duration) throws TrackIsNotExistsException;

    Page<Track> getAllTracksPagination(PageRequest pageRequest);

    Page<Track> getTracksByGenrePagination(PageRequest pageRequest, Genre genre);

    Page<Track> getTracksByMoodPagination(PageRequest pageRequest, Mood mood);

    Track addTrackGenre(Long trackId, Long genreId) throws TrackIsNotExistsException, GenreIsNotExistsException;

    Track removeTrackGenre(Long trackId, Long genreId) throws TrackIsNotExistsException, GenreIsNotExistsException, TrackHasNotGenreException;

    Track addTrackMood(Track track, Mood mood);

    Track removeTrackMood(Track track, Mood mood) throws MoodIsNotExistsException, TrackHasNotThisMoodException;

    Track addRating(Track track, Integer ratingValue, User user);

    void addTrackToUser(Long userId, Long trackId) throws TrackIsNotExistsException, UserIsNotExistsException;

    void removeTrackFromUser(Long userId, Long trackId) throws TrackIsNotExistsException, UserIsNotExistsException, UserHasNotTrackException;

    List<Track> saveAll(List<Track> tracks);

    List<Track> findAllByIds(List<Long> ids);

    List<Track> findAll();
}


