package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UsersDTO {

    private Integer allCount;
    private Set<UserDTO> users;

    public UsersDTO() {
    }

    public UsersDTO(Collection<User> dbModel, Integer allCount) {

        if (dbModel == null) {
            return;
        }

        this.users = dbModel.stream()
                .map(UserDTO::new)
                .collect(Collectors.toSet());
        this.allCount = allCount;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> friends) {
        this.users = friends;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }
}
