package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;

import java.util.Set;
import java.util.stream.Collectors;

public class FriendsDTO {

    private Set<UserDTO> friends;

    public FriendsDTO() {
    }

    public FriendsDTO(Set<User> dbModel) {

        if (dbModel == null) {
            return;
        }

        this.friends = dbModel.stream()
                .map(UserDTO::new)
                .collect(Collectors.toSet());
    }

    public Set<UserDTO> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserDTO> friends) {
        this.friends = friends;
    }
}
