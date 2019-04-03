package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.security.auth.Subject;
import java.security.Principal;

@Getter
@Setter
public class UserDTO implements Principal {

    private Long id;
    private String email;
    private String password;
    private String newPassword;
    private DateDTO dateCreate;
    private UserDetailsDTO userDetails;

    public UserDTO() {
    }

    public UserDTO(User dbModel) {

        if (dbModel == null) {
            return;
        }

        this.id = dbModel.getId();
        this.email = dbModel.getEmail();
        this.password = dbModel.getPassword();
        this.dateCreate = new DateDTO(dbModel.getDateCreate());
        this.userDetails = new UserDetailsDTO(dbModel.getUserDetails());
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public boolean implies(Subject subject) {
        return true;
    }
}
