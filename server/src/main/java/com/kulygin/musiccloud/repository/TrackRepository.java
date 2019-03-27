package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.domain.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface TrackRepository extends JpaRepository<Track,Long> {
    Track findByFilename(String filename);
    Page<Track> findAll(Pageable pageable);
    Page<Track> findAllByGenresContains(Pageable pageable, Set<Genre> genres);
    Page<Track> findAllByMoodsContains(Pageable pageable, Set<Mood> moods);
    List<Track> findAllByIdIn(List<Long> ids);
    @Query("select count(t) from Track t")
    int countAll();
}
