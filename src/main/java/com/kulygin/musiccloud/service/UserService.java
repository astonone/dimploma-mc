package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.UserHasExistsException;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;

import java.time.LocalDateTime;

public interface UserService {
    User findUserByEmail(String email);

    User getUserById(Long id);

    void deleteUserById(Long id) throws UserIsNotExistsException;

    User createUser(String email, String password) throws UserHasExistsException;

    User addUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday);

    User updateUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday);

    User save(User user);
}
