package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Mood;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodRepository extends JpaRepository<Mood,Long> {
    @Cacheable("mood")
    Mood findByName(String name);
    @Cacheable("mood")
    Page<Mood> findAll(Pageable pageable);
}
