package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
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
}
