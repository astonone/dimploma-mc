package com.kulygin.musiccloud.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DateDTO {

    private Integer year;
    private Integer month;
    private Integer day;
    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    public DateDTO() {
    }

    public DateDTO(LocalDateTime date) {
        if (date == null) {
            return;
        }
        year = date.getYear();
        month = date.getMonthValue();
        day = date.getDayOfMonth();
        hours = date.getHour();
        minutes = date.getMinute();
        seconds = date.getSecond();
    }
}
