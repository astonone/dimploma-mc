package com.kulygin.musiccloud.enumeration;

public enum ApplicationErrorTypes {
    USER_ID_NOT_FOUND(1, "User with this id has not found"),
    USER_HAS_EXISTS(2, "This user already exists"),
    IO_ERROR(3, "IO error via file download"),
    TRACK_ID_NOT_FOUND(4, "Track with this id has not found"),
    DB_IS_EMPTY_OR_PAGE_IS_NOT_EXIST(5, "Database is empty or page is not exists"),
    TRACK_HAS_EXISTS(6, "This track already exists"),
    FILE_NOT_FOUND(7, "File not found"),
    INVALID_DATA(8, "Invalid data"),
    UNSUPPORTED_TAG(9, "Tag is not unsupported"),
    GENRE_ID_NOT_FOUND(10, "Genre ID not found in DB"),
    MOOD_ID_NOT_FOUND(11, "Mood ID not found in DB"),
    PLAYLIST_ID_NOT_FOUND(12, "Playlist ID not found in DB"),
    MOOD_HAS_EXISTS(13, "Mood has exists in DB"),
    PLAYLIST_HAS_EXISTS(14, "Playlist has exists in DB"),
    GENRE_HAS_EXISTS(15, "Genre has exists in DB"),
    TRACK_HAS_NOT_GENRE(16, "This track has not genre"),
    PLAYLIST_HAS_NOT_TRACK(17, "This playlist has not track"),
    TRACK_HAS_NOT_THIS_MOOD(18, "This track has not this mood"),
    USER_HAS_NOT_TRACK(19, "User has not this track" ),
    USER_ALREADY_HAS_FRIEND(20,"These users already friends" ),
    REQUEST_ALREADY_SENT(21,"Request has been already sent" ),
    REQUEST_NOT_EXIST(21,"This request has not exists" ),
    USER_HAS_NOT_FRIEND_REQUESTS(22,"This user has not any friend requests" ),
    FRIEND_ID_NOT_FOUND(23,"This user has not this friend" ),
    PASSWORDS_DONT_MATCH(24, "Passwords don't match");

    private String message;
    private int code;

    ApplicationErrorTypes(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
