package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Mood;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

public class AllMoodsDTO {

    Set<MoodDTO> moods;

    public AllMoodsDTO() {
    }

    public AllMoodsDTO(Collection<Mood> dbModel) {

        if (dbModel == null) {
            return;
        }

        this.moods = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(MoodDTO::new)
                .collect(Collectors.toSet());
    }

    public Set<MoodDTO> getMoods() {
        return moods;
    }

    public void setMoods(Set<MoodDTO> moods) {
        this.moods = moods;
    }
}
