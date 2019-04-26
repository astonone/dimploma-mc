package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Genre;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Genre findByName(String name);
    @Cacheable("genre")
    Page<Genre> findAll(Pageable pageable);
    @Cacheable("genre")
    Set<Genre> findAllByIdIn(List<Long> ids);
}
