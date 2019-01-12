package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.dto.UserDetailsDTO;
import com.kulygin.musiccloud.exception.UserHasExistsException;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccount(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAccount(@PathVariable("id") Long userId) {
        try {
            userService.deleteUserById(userId);
        } catch (UserIsNotExistsException accountIsNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createAccount(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user;
        try {
            user = userService.createUser(email, password);
        } catch (UserHasExistsException accountHasExist) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_HAS_EXISTS);
        }
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/user_details", method = RequestMethod.PUT)
    public ResponseEntity<?> addAccountInfo(@PathVariable("id") Long userId, @RequestBody UserDetailsDTO info) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }

        LocalDateTime birthday = info.getBirthday() == null ? null : LocalDateTime.of(info.getBirthday().getYear(), info.getBirthday().getMonth(),
                info.getBirthday().getDay(), 0, 0);
        if (user.getUserDetails() == null) {
            user = userService.addUserDetails(user, info.getFirstName(), info.getLastName(), info.getPhotoLink(), info.getNick(), birthday);
        } else {
            user = userService.updateUserDetails(user, info.getFirstName(), info.getLastName(), info.getPhotoLink(), info.getNick(), birthday);
        }
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    private UserDTO convert(User dbModel) {
        return (dbModel == null) ? null : new UserDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
