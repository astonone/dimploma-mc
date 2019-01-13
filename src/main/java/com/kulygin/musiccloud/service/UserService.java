package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.*;

import java.time.LocalDateTime;
import java.util.Set;

public interface UserService {
    User findUserByEmail(String email);

    User getUserById(Long id);

    void deleteUserById(Long id) throws UserIsNotExistsException;

    User createUser(String email, String password) throws UserHasExistsException;

    User addUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday);

    User updateUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday);

    User save(User user);

    void sendFriendRequest(Long inviterId, Long friendId) throws UserIsNotExistsException, UserAlreadyHasFriendException, RequestAlreadySentException;

    void cancelFriendRequest(Long cancelerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException;

    void addFriend(Long inviterId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException;

    void removeFriend(Long removerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendException;

    Set<User> getAllFriendRequests(User user);
}
