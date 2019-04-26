package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.*;
import com.kulygin.musiccloud.dto.TrackFullInfoDTO;
import com.kulygin.musiccloud.exception.*;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TrackService {

    Track getTrackById(Long id);

    void deleteTrackById(Long id) throws TrackIsNotExistsException;

    Track createTrack(File file) throws TrackHasExistsException, IOException, UnsupportedTagException, FileIsNotExistsException, PlaylistNotExistsException, TrackIsNotExistsException, com.mpatric.mp3agic.InvalidDataException;

    Track updateTrack(Long trackId, String title, String artist, String album, Integer year, String filename, String duration) throws TrackIsNotExistsException;

    Page<Track> getAllTracksPagination(PageRequest pageRequest);

    Page<Track> getTracksByGenrePagination(PageRequest pageRequest, Genre genre);

    Page<Track> getTracksByMoodPagination(PageRequest pageRequest, Mood mood);

    Page<Track> getTracksByUserPagination(PageRequest id, User user);

    Page<Track> findTracks(PageRequest pageRequest, TrackFullInfoDTO trackFullInfoDTO);

    int countTracks(TrackFullInfoDTO trackFullInfoDTO);

    int countTracksByUserPagination(User user);

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

    int countAll();
}


