package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Mood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRepository extends JpaRepository<Mood,Long> {
    Mood findByName(String name);
    List<Mood> findAllByOrderByNameAsc();
    List<Mood> findAllByIdInOrderByNameAsc(List<Long> ids);
}
