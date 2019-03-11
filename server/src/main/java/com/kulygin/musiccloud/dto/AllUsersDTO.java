package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

public class AllUsersDTO {
    List<UserDTO> users;

    public AllUsersDTO() {
    }

    public AllUsersDTO(Collection<User> dbModel) {
        if (dbModel == null) {
            return;
        }
        this.users = ofNullable(dbModel).orElse(newArrayList()).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
