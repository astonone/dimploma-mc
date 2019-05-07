package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.dto.UserDetailsDTO;
import com.kulygin.musiccloud.dto.UsersDTO;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.exception.*;
import com.kulygin.musiccloud.service.UserService;
import com.kulygin.musiccloud.service.impl.yandex.YandexAPI;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator.GeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private YandexAPI yandexAPI;

    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        User userByEmail = userService.findUserByEmail(user.getEmail());
        if (userByEmail != null) {
            if (passwordEncoder.matches(user.getPassword(), userByEmail.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @RequestMapping("/auth")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
        String email = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
        User userByEmail = userService.findUserByEmail(email);
        return convert(userByEmail);
    }

    @RequestMapping(value = "{id}/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadToYandexDisk(@PathVariable("id") Long userId, @RequestParam("uploadedFile") MultipartFile uploadedFileRef) {
        User user = userService.getUserById(userId);
        File file = null;
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        try {
            file = yandexAPI.uploadFileToYandexDisk(uploadedFileRef, true);
        } catch (Exception e) {
            return getErrorResponseBody(ApplicationErrorTypes.INVALID_DATA);
        }
        user = userService.uploadPhoto(user, file.getName());
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @GetMapping("/getYandex/{filename:.+}")
    public ResponseEntity<List<String>> getFileFromYaDisk(@PathVariable String filename) {
        List<String> fileNames = Arrays.asList(MvcUriComponentsBuilder.fromMethodName(UserController.class, "getFile", filename).build().toString());
        return ResponseEntity.ok().body(fileNames);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = yandexAPI.loadPhotoFileFromYandexDisk(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @RequestMapping(value = "/{id}/deletePhoto", method = RequestMethod.POST)
    public ResponseEntity<?> deleteUserPhoto(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        user = userService.deletePhoto(user);
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccount(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @RequestMapping(value = "email/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> getAccountByEmail(@PathVariable("email") String email) {
        User user = userService.findUserByEmail(email);
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateAccount(@RequestBody UserDTO userDTO) {
        User user = userService.updateUser(userDTO);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.PASSWORDS_DONT_MATCH);
        }
        return new ResponseEntity<>(convert(user), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/user_details", method = RequestMethod.PUT)
    public ResponseEntity<?> addUserDetails(@PathVariable("id") Long userId, @RequestBody UserDetailsDTO info) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }

        LocalDateTime birthday = info.getBirthday() == null ? null : info.getBirthday().getYear() == null ? null : LocalDateTime.of(info.getBirthday().getYear(), info.getBirthday().getMonth(),
                info.getBirthday().getDay(), 0, 0);
        if (user.getUserDetails() == null) {
            user = userService.addUserDetails(user, info.getFirstName(), info.getLastName(), info.getPhotoLink(), info.getNick(), birthday, info.getAbout());
        } else {
            user = userService.updateUserDetails(user, info.getFirstName(), info.getLastName(), info.getPhotoLink(), info.getNick(), birthday, info.getAbout());
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

    @RequestMapping(value = "/{id}/cancelFriendRequest", method = RequestMethod.POST)
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

    @RequestMapping(value = "/{id}/removeFriend", method = RequestMethod.POST)
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
        return new ResponseEntity<>(convertUserList(requests, null), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/friends", method = RequestMethod.GET)
    public ResponseEntity<?> getAllFriends(@PathVariable("id") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        Set<User> friends = userService.getAllFriends(user);
        return new ResponseEntity<>(convertUserList(friends, null), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        Page<User> users = userService.getUsersPagination(PageRequest.of(page.intValue(), pageSize.intValue(), new Sort(Sort.Direction.ASC, "id")));
        List<User> resultListOfUsers = users.getContent();
        int count = userService.countAll();
        return new ResponseEntity<>(convertUserList(resultListOfUsers, count), HttpStatus.OK);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<?> findUsers(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize, @RequestParam("firstName") String firstName,
                                       @RequestParam("lastName") String lastName, @RequestParam("nickName") String nickName) {
        Page<User> users = userService.findUsers(PageRequest.of(page.intValue(), pageSize.intValue(), new Sort(Sort.Direction.ASC, "id")), firstName, lastName, nickName);
        List<User> resultListOfUsers = users.getContent();
        int count = userService.countUsers(firstName, lastName, nickName);
        return new ResponseEntity<>(convertUserList(resultListOfUsers, count), HttpStatus.OK);
    }

    private UsersDTO convertUserList(Collection<User> dbModel, Integer count) {
        return (dbModel == null) ? null : new UsersDTO(dbModel, count);
    }

    private UserDTO convert(User dbModel) {
        return (dbModel == null) ? null : new UserDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
