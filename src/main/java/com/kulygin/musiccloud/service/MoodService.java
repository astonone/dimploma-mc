package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.exception.MoodHasExistsException;
import com.kulygin.musiccloud.exception.MoodIsNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface MoodService {

    Mood getMoodById(Long id);

    void deleteMoodById(Long id) throws MoodIsNotExistsException;

    Mood createMood(String name) throws MoodHasExistsException;

    Page<Mood> getAllMoodsPagination(PageRequest pageRequest);
}
