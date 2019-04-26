package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Mood;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MoodRepository extends JpaRepository<Mood,Long> {
    Mood findByName(String name);
    @Cacheable("mood")
    Page<Mood> findAll(Pageable pageable);
    @Cacheable("mood")
    Set<Mood> findAllByIdIn(List<Long> ids);
}
