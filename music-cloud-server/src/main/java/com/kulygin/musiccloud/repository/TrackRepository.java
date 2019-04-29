package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Track findByFilename(String filename);
    Page<Track> findAll(Pageable pageable);
    Page<Track> findAllDistinctByGenresContains(Pageable pageable, Set<Genre> genres);
    Page<Track> findAllDistinctByMoodsContains(Pageable pageable, Set<Mood> moods);
    int countAllDistinctByGenresContains(Set<Genre> genres);
    int countAllDistinctByMoodsContains(Set<Mood> moods);
    Page<Track> findAllByUsersContains(Pageable pageable, Set<User> users);
    List<Track> findAllByUsersContains(Set<User> users);
    List<Track> findAllByIdIn(List<Long> ids);
    @Query("select count(t) from Track t")
    int countAll();
    Page<Track> findAllByTitleOrArtistOrGenresInOrMoodsIn(Pageable pageable, String title, String artist, Set<Genre> genres, Set<Mood> moods);
    int countAllByTitleOrArtistOrGenresInOrMoodsIn(String title, String artist, Set<Genre> genres, Set<Mood> moods);
}
