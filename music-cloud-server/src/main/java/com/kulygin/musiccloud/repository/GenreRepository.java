package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre,Long> {
    Genre findByName(String name);
    Page<Genre> findAll(Pageable pageable);
}
