package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TrackRepository extends JpaRepository<Track,Long> {
    @Cacheable("track")
    Track findByFilename(String filename);
    @Cacheable("track")
    Page<Track> findAll(Pageable pageable);
    @Cacheable("track")
    Page<Track> findAllByGenresContains(Pageable pageable, Set<Genre> genres);
    @Cacheable("track")
    Page<Track> findAllByMoodsContains(Pageable pageable, Set<Mood> moods);
    @Cacheable("track")
    Page<Track> findAllByUsersContains(Pageable pageable, Set<User> users);
    @Cacheable("track")
    List<Track> findAllByUsersContains(Set<User> users);
    @Cacheable("track")
    List<Track> findAllByIdIn(List<Long> ids);
    @Cacheable("track")
    @Query("select count(t) from Track t")
    int countAll();
}
