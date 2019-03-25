package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.UserDetails;

public class UserDetailsDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String nick;
    private String about;
    private String photoLink;
    private DateDTO birthday;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(UserDetails userDetails) {
        if (userDetails == null) {
            return;
        }
        this.id = userDetails.getId();
        this.firstName = userDetails.getFirstName();
        this.lastName = userDetails.getLastName();
        this.nick = userDetails.getNickName();
        this.about = userDetails.getAbout();
        this.photoLink = userDetails.getPhotoLink();
        this.birthday = new DateDTO(userDetails.getBirthday());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }

    public DateDTO getBirthday() {
        return birthday;
    }

    public void setBirthday(DateDTO birthday) {
        this.birthday = birthday;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
