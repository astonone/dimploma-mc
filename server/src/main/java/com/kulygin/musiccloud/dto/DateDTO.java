package com.kulygin.musiccloud.dto;

import java.time.LocalDateTime;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
