package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.Mood;
import com.kulygin.musiccloud.exception.MoodHasExistsException;
import com.kulygin.musiccloud.exception.MoodIsNotExistsException;
import com.kulygin.musiccloud.repository.MoodRepository;
import com.kulygin.musiccloud.service.MoodService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class MoodServiceImpl implements MoodService {

    @Autowired
    private MoodRepository moodRepository;

    @Override
    public Mood getMoodById(Long id) {
        return moodRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteMoodById(Long id) throws MoodIsNotExistsException {
        if (!moodRepository.existsById(id)) {
            throw new MoodIsNotExistsException();
        }
        moodRepository.deleteById(id);
    }

    @Override
    public Mood createMood(String name) throws MoodHasExistsException {
        Mood mood = moodRepository.findByName(name);
        if (mood != null) {
            log.error("Mood has not found: " + name);
            throw new MoodHasExistsException();
        }
        return moodRepository.save(Mood.builder()
                .name(name)
                .build());
    }

    @Override
    public Page<Mood> getAllMoodsPagination(PageRequest pageRequest) {
        return moodRepository.findAll(pageRequest);
    }
}
