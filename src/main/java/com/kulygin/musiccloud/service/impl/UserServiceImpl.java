package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.domain.UserDetails;
import com.kulygin.musiccloud.exception.UserHasExistsException;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;
import com.kulygin.musiccloud.repository.UserDetailsRepository;
import com.kulygin.musiccloud.repository.UserRepository;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

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
            throw new UserIsNotExistsException();
        } else {
            userRepository.deleteById(id);
        }
    }

    @Override
    public User createUser(String email, String password) throws UserHasExistsException {
        User user = findUserByEmail(email);
        if (user != null) {
            throw new UserHasExistsException();
        } else {
            return userRepository.save(User.builder()
                    .email(email)
                    .password(password)
                    .dateCreate(LocalDateTime.now())
                    .build());
        }
    }

    @Override
    public User addUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday) {
        UserDetails userDetails = UserDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .photoLink(photoLink)
                .nickName(nickName)
                .birthday(birthday)
                .build();
        userDetails = userDetailsRepository.save(userDetails);
        user.setUserDetails(userDetails);
        return userRepository.save(user);
    }

    @Override
    public User updateUserDetails(User user, String firstName, String lastName, String photoLink, String nickName, LocalDateTime birthday) {
        UserDetails userDetails = user.getUserDetails();

        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setPhotoLink(photoLink);
        userDetails.setNickName(nickName);
        userDetails.setBirthday(birthday);

        userDetails = userDetailsRepository.save(userDetails);
        user.setUserDetails(userDetails);
        return userRepository.save(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
