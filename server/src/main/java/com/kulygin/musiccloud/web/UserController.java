package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.dto.FriendsDTO;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.dto.UserDetailsDTO;
import com.kulygin.musiccloud.exception.*;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        return user.getEmail().equals("user") && user.getPassword().equals("user");
    }

    @RequestMapping("/auth")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }

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
    public ResponseEntity<?> addUserDetails(@PathVariable("id") Long userId, @RequestBody UserDetailsDTO info) {
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

    @RequestMapping(value = "/{id}/sendFriendRequest", method = RequestMethod.POST)
    public ResponseEntity<?> sendFriendRequest(@PathVariable("id") Long inviterId, @RequestParam("friendId") Long friendId) {
        try {
            userService.sendFriendRequest(inviterId, friendId);
        } catch (UserIsNotExistsException userIsNotExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (UserAlreadyHasFriendException userAlreadyHasFriend) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ALREADY_HAS_FRIEND);
        } catch (RequestAlreadySentException requestAlreadySent) {
            return getErrorResponseBody(ApplicationErrorTypes.REQUEST_ALREADY_SENT);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/cancelFriendRequest", method = RequestMethod.DELETE)
    public ResponseEntity<?> cancelFriendRequest(@PathVariable("id") Long cancelerId, @RequestParam("friendId") Long friendId) {
        try {
            userService.cancelFriendRequest(cancelerId, friendId);
        } catch (UserIsNotExistsException userIsNotExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (RequestNotExistException requestNotExist) {
            return getErrorResponseBody(ApplicationErrorTypes.REQUEST_NOT_EXIST);
        } catch (UserHasNotFriendRequestException userHasNotFriendRequestException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_HAS_NOT_FRIEND_REQUESTS);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/applyFriendRequest", method = RequestMethod.POST)
    public ResponseEntity<?> addFriend(@PathVariable("id") Long friendId, @RequestParam("inviterId") Long inviterId) {
        try {
            userService.addFriend(inviterId, friendId);
        } catch (UserIsNotExistsException userIsNotExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (RequestNotExistException requestNotExist) {
            return getErrorResponseBody(ApplicationErrorTypes.REQUEST_NOT_EXIST);
        } catch (UserHasNotFriendRequestException userHasNotFriendRequestException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_HAS_NOT_FRIEND_REQUESTS);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/removeFriend", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeFriend(@PathVariable("id") Long removerId, @RequestParam("friendId") Long friendId) {
        try {
            userService.removeFriend(removerId, friendId);
        } catch (UserIsNotExistsException userIsNotExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (UserHasNotFriendException userHasNotFriendException) {
            return getErrorResponseBody(ApplicationErrorTypes.FRIEND_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/requests", method = RequestMethod.GET)
    public ResponseEntity<?> getAllAccountRequests(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        Set<User> requests = userService.getAllFriendRequests(user);
        return new ResponseEntity<>(convertFriendList(requests),HttpStatus.OK);
    }

    private FriendsDTO convertFriendList(Set<User> dbModel) { return (dbModel == null) ? null : new FriendsDTO(dbModel); }

    private UserDTO convert(User dbModel) {
        return (dbModel == null) ? null : new UserDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
