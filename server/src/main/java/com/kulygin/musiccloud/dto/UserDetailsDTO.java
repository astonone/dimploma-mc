package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.UserDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
