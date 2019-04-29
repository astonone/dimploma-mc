package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.*;
import com.kulygin.musiccloud.dto.GenreDTO;
import com.kulygin.musiccloud.dto.MoodDTO;
import com.kulygin.musiccloud.dto.TrackFullInfoDTO;
import com.kulygin.musiccloud.exception.*;
import com.kulygin.musiccloud.repository.TrackRepository;
import com.kulygin.musiccloud.service.*;
import com.mpatric.mp3agic.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private GenreService genreService;
    @Autowired
    private MoodService moodService;
    @Autowired
    private UserService userService;
    @Autowired
    private StatisticalAccountingService statisticalAccountingService;

    @Override
    public Track getTrackById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteTrackById(Long id) throws TrackIsNotExistsException {
        if (!trackRepository.existsById(id)) {
            throw new TrackIsNotExistsException();
        }
        trackRepository.deleteById(id);
    }

    @Override
    public Track createTrack(File file) throws TrackHasExistsException, IOException,
            UnsupportedTagException, FileIsNotExistsException, InvalidDataException {
        return parsingMp3File(file);
    }

    @Override
    public Track updateTrack(Long trackId, String title, String album, String artist, Integer year, String filename, String duration) throws TrackIsNotExistsException {
        Optional<Track> track = trackRepository.findById(trackId);
        if (!track.isPresent()) {
            throw new TrackIsNotExistsException();
        }
        Track trackForUpdate = track.get();
        if (title != null) {
            trackForUpdate.setTitle(title);
        }
        if (artist != null) {
            trackForUpdate.setArtist(artist);
        }
        if (album != null) {
            trackForUpdate.setAlbum(album);
        }
        if (year != null) {
            trackForUpdate.setYear(year);
        }
        if (filename != null) {
            trackForUpdate.setFilename(filename);
        }
        if (duration != null) {
            trackForUpdate.setDuration(duration);
        }
        return trackRepository.save(trackForUpdate);
    }

    @Override
    public Page<Track> getAllTracksPagination(PageRequest pageRequest) {
        return trackRepository.findAll(pageRequest);
    }

    @Override
    public Page<Track> getTracksByGenrePagination(PageRequest pageRequest, Genre genre) {
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        return trackRepository.findAllDistinctByGenresContains(pageRequest, genres);
    }

    @Override
    public Page<Track> getTracksByMoodPagination(PageRequest pageRequest, Mood mood) {
        Set<Mood> moods = new HashSet<>();
        moods.add(mood);
        return trackRepository.findAllDistinctByMoodsContains(pageRequest, moods);
    }

    @Override
    public int countTracksByGenrePagination(Genre genre) {
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        return trackRepository.countAllDistinctByGenresContains(genres);
    }

    @Override
    public int countTracksByMoodPagination(Mood mood) {
        Set<Mood> moods = new HashSet<>();
        moods.add(mood);
        return trackRepository.countAllDistinctByMoodsContains(moods);
    }

    @Override
    public Page<Track> getTracksByUserPagination(PageRequest pageRequest, User user) {
        Set<User> users = new HashSet<>();
        users.add(user);
        return trackRepository.findAllByUsersContains(pageRequest, users);
    }

    @Override
    public Page<Track> findTracks(PageRequest pageRequest, TrackFullInfoDTO trackFullInfoDTO) {
        return trackRepository.findAllByTitleOrArtistOrGenresInOrMoodsIn(pageRequest, trackFullInfoDTO.getTitle(), trackFullInfoDTO.getArtist(),
                genreService.findAllByIds(trackFullInfoDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toList())), moodService.findAllByIds(trackFullInfoDTO.getMoods().stream().map(MoodDTO::getId).collect(Collectors.toList())));
    }

    @Override
    public int countTracks(TrackFullInfoDTO trackFullInfoDTO) {
        return trackRepository.countAllByTitleOrArtistOrGenresInOrMoodsIn(trackFullInfoDTO.getTitle(), trackFullInfoDTO.getArtist(),
                genreService.findAllByIds(trackFullInfoDTO.getGenres().stream().map(GenreDTO::getId).collect(Collectors.toList())), moodService.findAllByIds(trackFullInfoDTO.getMoods().stream().map(MoodDTO::getId).collect(Collectors.toList())));
    }

    @Override
    public int countTracksByUserPagination(User user) {
        Set<User> users = new HashSet<>();
        users.add(user);
        return trackRepository.findAllByUsersContains(users).size();
    }

    @Override
    public Track addTrackGenre(Long trackId, Long genreId) throws TrackIsNotExistsException, GenreIsNotExistsException {
        Track track = getTrackById(trackId);
        if (track == null) {
            log.error("Track has not found: " + trackId);
            throw new TrackIsNotExistsException();
        }
        Genre genre = genreService.getGenreById(genreId);
        if (genre == null) {
            log.error("Genre has not found: " + genreId);
            throw new GenreIsNotExistsException();
        }
        Set<Genre> genres = track.getGenres();
        genres.add(genre);
        track.setGenres(genres);
        return trackRepository.save(track);
    }

    @Override
    public Track removeTrackGenre(Long trackId, Long genreId) throws TrackIsNotExistsException, GenreIsNotExistsException, TrackHasNotGenreException {
        Track track = getTrackById(trackId);
        if (track == null) {
            log.error("Track has not found: " + trackId);
            throw new TrackIsNotExistsException();
        }
        Genre genre = genreService.getGenreById(genreId);
        if (genre == null) {
            log.error("Genre has not found: " + genreId);
            throw new GenreIsNotExistsException();
        }
        Set<Genre> genres = track.getGenres();
        if(genres.contains(genre)) {
            genres.remove(genre);
        } else {
            throw new TrackHasNotGenreException();
        }
        track.setGenres(genres);
        return trackRepository.save(track);
    }

    @Override
    public Track addTrackMood(Track track, Mood mood) {
        Set<Mood> trackMoods = track.getMoods();
        trackMoods.add(mood);
        track.setMoods(trackMoods);
        return trackRepository.save(track);
    }

    @Override
    public Track removeTrackMood(Track track, Mood mood) throws TrackHasNotThisMoodException {
        Set<Mood> moods = track.getMoods();
        if (moods.contains(mood)) {
            moods.remove(mood);
        } else {
            throw new TrackHasNotThisMoodException();
        }
        track.setMoods(moods);
        return trackRepository.save(track);
    }

    @Override
    public Track addRating(Track track, Integer ratingValue, User user) {
        StatisticalAccounting statisticalAccounting = statisticalAccountingService.findByUserAndTrack(user.getId(), track.getId());

        Long countOfRate = track.getCountOfRate();
        Long sumOfRatings = track.getSumOfRatings();
        Double rating = 0d;

        if (statisticalAccounting != null) {
            sumOfRatings += ratingValue - statisticalAccounting.getRatingValue().longValue();
            rating = Double.valueOf(new DecimalFormat("#.00").format(sumOfRatings/countOfRate.doubleValue()));
        } else {
            if (countOfRate == null || sumOfRatings == null) {
                countOfRate = 1L;
                sumOfRatings = ratingValue.longValue();
                rating = ratingValue.doubleValue();
            } else {
                countOfRate++;
                sumOfRatings += ratingValue.longValue();
                rating = Double.valueOf(new DecimalFormat("#.00").format(sumOfRatings/countOfRate.doubleValue()));
            }
        }

        track.setCountOfRate(countOfRate);
        track.setSumOfRatings(sumOfRatings);
        track.setRating(rating);
        // Save statistics
        if (statisticalAccounting == null) {
            statisticalAccounting = StatisticalAccounting.builder()
                    .userId(user.getId())
                    .trackId(track.getId())
                    .ratingValue(ratingValue)
                    .build();
        } else {
            statisticalAccounting.setRatingValue(ratingValue);
        }

        statisticalAccountingService.save(statisticalAccounting);
        return trackRepository.save(track);
    }

    @Override
    public void addTrackToUser(Long userId, Long trackId) throws TrackIsNotExistsException, UserIsNotExistsException {
        Track track = getTrackById(trackId);
        if (track == null) {
            throw new TrackIsNotExistsException();
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Set<User> users = track.getUsers();
        users.add(user);
        track.setUsers(users);
        trackRepository.save(track);
    }

    @Override
    public void removeTrackFromUser(Long userId, Long trackId) throws TrackIsNotExistsException, UserIsNotExistsException, UserHasNotTrackException {
        Track track = getTrackById(trackId);
        if (track == null) {
            throw new TrackIsNotExistsException();
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Set<User> users = track.getUsers();
        if (users.contains(user)) {
            users.remove(user);
        } else {
            throw new UserHasNotTrackException();
        }
        track.setUsers(users);
        trackRepository.save(track);
    }

    @Override
    public List<Track> saveAll(List<Track> tracks) {
        return trackRepository.saveAll(tracks);
    }

    @Override
    public List<Track> findAllByIds(List<Long> ids) {
        return trackRepository.findAllByIdIn(ids);
    }

    @Override
    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    @Override
    public int countAll() {
        return trackRepository.countAll();
    }

    private Track parsingMp3File(File file) throws InvalidDataException, IOException, UnsupportedTagException, FileIsNotExistsException, TrackHasExistsException {
        Mp3File mp3File = null;
        try {
            mp3File = new Mp3File(file);
            file.delete();
        } catch (FileNotFoundException fileNotFound) {
            log.error("File is not exists");
            throw new FileIsNotExistsException();
        }
        Track track = trackRepository.findByFilename(file.getName());
        if (track != null) {
            log.error("Track is not exists");
            throw new TrackHasExistsException();
        }
        String title = "";
        String artist = "";
        String album = "";
        String duration;
        Integer year = 0;
        String genre = "";
        duration = mp3File.getLengthInSeconds() + " cекунд";
        if (mp3File.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3File.getId3v1Tag();
            title = id3v1Tag.getTitle();
            artist = id3v1Tag.getArtist();
            album = id3v1Tag.getAlbum();
            if (id3v1Tag.getYear() == null) {
                year = 0;
            } else {
                year = Integer.parseInt(id3v1Tag.getYear());
            }
            genre = id3v1Tag.getGenreDescription();
        }
        if (mp3File.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
            title = id3v2Tag.getTitle();
            artist = id3v2Tag.getArtist();
            album = id3v2Tag.getAlbum();
            if (id3v2Tag.getYear() == null) {
                year = 0;
            } else {
                year = Integer.parseInt(id3v2Tag.getYear());
            }
            genre = id3v2Tag.getGenreDescription();
        }
        track = Track.builder()
                .title(title)
                .album(album)
                .artist(artist)
                .year(year)
                .filename(file.getName())
                .duration(duration)
                .build();
        return trackRepository.save(track);
    }
}

