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
    List<Track> findAllByOrderByTitleAsc();
    Page<Track> findAllDistinctByGenresContainsOrderByTitleAsc(Pageable pageable, Set<Genre> genres);
    Page<Track> findAllDistinctByMoodsContainsOrderByTitleAsc(Pageable pageable, Set<Mood> moods);
    int countAllDistinctByGenresContains(Set<Genre> genres);
    int countAllDistinctByMoodsContains(Set<Mood> moods);
    Page<Track> findAllByUsersContainsOrderByTitleAsc(Pageable pageable, Set<User> users);
    List<Track> findAllByUsersContainsOrderByTitleAsc(Set<User> users);
    List<Track> findAllByIdInOrderByTitle(List<Long> ids);
    @Query("select count(t) from Track t")
    int countAll();
    Page<Track> findAllDistinctByTitleOrArtistOrGenresInOrMoodsInOrderByTitleAsc(Pageable pageable, String title, String artist, List<Genre> genres, List<Mood> moods);
    int countAllDistinctByTitleOrArtistOrGenresInOrMoodsIn(String title, String artist, List<Genre> genres, List<Mood> moods);
}
