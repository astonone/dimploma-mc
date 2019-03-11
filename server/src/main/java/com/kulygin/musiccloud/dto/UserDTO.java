package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;

import java.util.List;

public class UserDTO {

    private Long id;
    private String email;
    private String password;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DateDTO getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(DateDTO dateCreate) {
        this.dateCreate = dateCreate;
    }

    public UserDetailsDTO getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetailsDTO userDetails) {
        this.userDetails = userDetails;
    }
}
