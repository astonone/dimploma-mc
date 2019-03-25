package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Mood;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoodDTO {

    private Long id;
    private String name;

    public MoodDTO() {
    }

    public MoodDTO(Mood dbModel) {

        if (dbModel == null) {
            return;
        }

        this.id = dbModel.getId();
        this.name = dbModel.getName();
    }
}
