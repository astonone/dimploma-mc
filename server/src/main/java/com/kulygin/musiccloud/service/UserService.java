package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface UserService {
    User findUserByEmail(String email);

    User getUserById(Long id);

    void deleteUserById(Long id) throws UserIsNotExistsException;

    User createUser(String email, String password) throws UserHasExistsException;

    User addUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday, String about);

    User updateUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday, String about);

    User save(User user);

    void sendFriendRequest(Long inviterId, Long friendId) throws UserIsNotExistsException, UserAlreadyHasFriendException, RequestAlreadySentException;

    void cancelFriendRequest(Long cancelerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException;

    void addFriend(Long inviterId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException;

    void removeFriend(Long removerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendException;

    Set<User> getAllFriendRequests(User user);

    List<User> saveAll(List<User> users);

    List<User> findAll();

    User uploadPhoto(User user, String fileName);

    Page<User> getUsersPagination(PageRequest request);

    int countAll();

    User updateUser(UserDTO userDTO);
}
