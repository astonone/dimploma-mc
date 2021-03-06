package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.domain.UserDetails;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.exception.*;
import com.kulygin.musiccloud.repository.UserDetailsRepository;
import com.kulygin.musiccloud.repository.UserRepository;
import com.kulygin.musiccloud.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Log4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserById(Long id) throws UserIsNotExistsException {
        User user = getUserById(id);
        if (user == null) {
            log.error("User has not found: " + id);
            throw new UserIsNotExistsException();
        } else {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User createUser(String email, String password) throws UserHasExistsException {
        User user = findUserByEmail(email);
        if (user != null) {
            log.error("User has exist: " + email);
            throw new UserHasExistsException();
        } else {
            return userRepository.save(User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .dateCreate(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public User addUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday, String about) {
        UserDetails userDetails = UserDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .photoLink(photoLink)
                .nickName(nickName)
                .birthday(birthday)
                .about(about)
                .build();
        userDetails = userDetailsRepository.save(userDetails);
        user.setUserDetails(userDetails);
        return userRepository.save(user);
    }

    @Override
    public User updateUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday, String about) {
        UserDetails userDetails = user.getUserDetails();

        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setPhotoLink(photoLink);
        userDetails.setNickName(nickName);
        userDetails.setBirthday(birthday);
        userDetails.setAbout(about);

        userDetails = userDetailsRepository.save(userDetails);
        user.setUserDetails(userDetails);
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void sendFriendRequest(Long inviterId, Long friendId) throws UserIsNotExistsException, UserAlreadyHasFriendException, RequestAlreadySentException {
        User inviter = getUserById(inviterId);
        if (inviter == null) {
            log.error("User has not found: " + inviterId);
            throw new UserIsNotExistsException();
        }
        User friend = getUserById(friendId);
        if (friend == null) {
            log.error("User has not found: " + friendId);
            throw new UserIsNotExistsException();
        }
        if (inviter.getFriends() != null) {
            if (inviter.getFriends().contains(friend)) {
                throw new UserAlreadyHasFriendException();
            }
        }
        if (friend.getFriendRequests() != null) {
            if (friend.getFriendRequests().contains(inviter)) {
                throw new RequestAlreadySentException();
            }
        }
        Set<User> friendRequests = friend.getFriendRequests();
        if (friendRequests == null) {
            friendRequests = new HashSet<>();
        }
        friendRequests.add(inviter);
        friend.setFriendRequests(friendRequests);
        userRepository.save(friend);
    }

    @Override
    public void cancelFriendRequest(Long cancelerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException {
        User canceler = getUserById(cancelerId);
        if (canceler == null) {
            log.error("User has not found: " + cancelerId);
            throw new UserIsNotExistsException();
        }
        User friend = getUserById(friendId);
        if (friend == null) {
            log.error("User has not found: " + friendId);
            throw new UserIsNotExistsException();
        }
        if (canceler.getFriendRequests().size() == 0) {
            throw new UserHasNotFriendRequestException();
        }
        if (canceler.getFriendRequests().contains(friend)) {
            canceler.getFriendRequests().remove(friend);
            userRepository.save(canceler);
        } else {
            throw new RequestNotExistException();
        }
    }

    @Override
    public void addFriend(Long inviterId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendRequestException, RequestNotExistException {
        User inviter = getUserById(inviterId);
        if (inviter == null) {
            log.error("User has not found: " + inviterId);
            throw new UserIsNotExistsException();
        }
        User friend = getUserById(friendId);
        if (friend == null) {
            log.error("User has not found: " + friendId);
            throw new UserIsNotExistsException();
        }
        if (friend.getFriendRequests().size() == 0) {
            throw new UserHasNotFriendRequestException();
        }
        if (friend.getFriendRequests().contains(inviter)) {
            friend.getFriendRequests().remove(inviter);

            friend.getFriends().add(inviter);
            userRepository.save(friend);

            inviter.getFriends().add(friend);
            userRepository.save(inviter);
        } else {
            throw new RequestNotExistException();
        }
    }

    @Override
    public void removeFriend(Long removerId, Long friendId) throws UserIsNotExistsException, UserHasNotFriendException {
        User remover = getUserById(removerId);
        if (remover == null) {
            log.error("User has not found: " + removerId);
            throw new UserIsNotExistsException();
        }
        User friend = getUserById(friendId);
        if (friend == null) {
            log.error("User has not found: " + friendId);
            throw new UserIsNotExistsException();
        }
        if (remover.getFriends().contains(friend)) {
            remover.getFriends().remove(friend);
            userRepository.save(remover);

            friend.getFriends().remove(remover);
            userRepository.save(friend);
        } else {
            throw new UserHasNotFriendException();
        }

    }

    @Override
    public Set<User> getAllFriendRequests(User user) {
        return user.getFriendRequests();
    }

    @Override
    public List<User> saveAll(List<User> users) {
        return userRepository.saveAll(users);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User uploadPhoto(User user, String fileName) {
        UserDetails userDetails = user.getUserDetails();
        userDetails.setPhotoLink(fileName);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersPagination(PageRequest request, Long userId) {
        return new ArrayList<>(userRepository.findAllByIdNotOrderByUserDetails_LastNameAsc(request, userId).getContent());
    }

    @Override
    public Page<User> findUsers(PageRequest request, String firstName, String lastName, String nickName) {
        return userRepository.findAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickNameOrderByUserDetails_LastNameAsc(request, firstName, lastName, nickName);
    }

    @Override
    public int countUsers(String firstName, String lastName, String nickName) {
        return userRepository.countAllByUserDetails_FirstNameOrUserDetails_LastNameOrUserDetails_NickName(firstName, lastName, nickName);
    }

    @Override
    public int countAll() {
        return userRepository.countAll();
    }

    @Override
    public int countAllByIdNot(Long userId) {
        return userRepository.countAllByIdNot(userId);
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        if (userDTO.getEmail().equals("") || userDTO.getPassword().equals("") || userDTO.getNewPassword().equals("")) {
            return null;
        }
        User user = getUserById(userDTO.getId());
        if (user != null) {
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                user.setEmail(userDTO.getEmail());
                user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));

                user = userRepository.save(user);
            } else {
                user = null;
            }
        }
        return user;
    }

    @Override
    public User deletePhoto(User user) {
        UserDetails userDetails = user.getUserDetails();
        userDetails.setPhotoLink(null);
        return userRepository.save(user);
    }

    @Override
    public Set<User> getAllFriends(User user) {
        return user.getFriends();
    }
}
