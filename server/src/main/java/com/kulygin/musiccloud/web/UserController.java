package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.config.Constants;
import com.kulygin.musiccloud.domain.Genre;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.dto.UsersDTO;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.UserDTO;
import com.kulygin.musiccloud.dto.UserDetailsDTO;
import com.kulygin.musiccloud.exception.*;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }

    @RequestMapping(value = "/{id}/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadUserPhoto(@PathVariable("id") Long userId, @RequestParam("uploadedFile") MultipartFile uploadedFileRef) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        // Получаем имя загруженного файла
        String fileName;
        // Генерируем уникальное имя файла
        UUID uuid = UUID.randomUUID();
        fileName = uuid.toString() + uploadedFileRef.getOriginalFilename().substring(uploadedFileRef.getOriginalFilename().lastIndexOf("."));
        // Путь, где загруженный файл будет сохранен.
        String path = System.getProperty("user.dir") + "/" + Constants.DOWNLOAD_PHOTO_PATH + fileName;
        // Буффер для хранения данных из uploadedFileRef
        byte[] buffer = new byte[1000];
        // Теперь создаем выходной файл outputFile на сервере
        File outputFile = new File(path);

        FileInputStream reader = null;
        FileOutputStream writer = null;
        int totalBytes = 0;
        try {
            outputFile.createNewFile();
            // Создаем входной поток для чтения данных из него
            reader = (FileInputStream) uploadedFileRef.getInputStream();
            // Создаем выходной поток для записи данных
            writer = new FileOutputStream(outputFile);
            // Считываем данные uploadedFileRef и пишем их в outputFile
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) != -1) {
                writer.write(buffer);
                totalBytes += bytesRead;
            }
        } catch (IOException iO) {
            return getErrorResponseBody(ApplicationErrorTypes.IO_ERROR);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException iO) {
                return getErrorResponseBody(ApplicationErrorTypes.IO_ERROR);
            }
        }
        user = userService.uploadPhoto(user, fileName);
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

    @RequestMapping(value = "/{id}/user_details", method = RequestMethod.PUT)
    public ResponseEntity<?> addUserDetails(@PathVariable("id") Long userId, @RequestBody UserDetailsDTO info) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }

        LocalDateTime birthday = info.getBirthday() == null ? null : LocalDateTime.of(info.getBirthday().getYear(), info.getBirthday().getMonth(),
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
        return new ResponseEntity<>(convertUserList(requests, null),HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsers(@RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize) {
        Page<User> users = userService.getUsersPagination(PageRequest.of(page.intValue(), pageSize.intValue(), new Sort(Sort.Direction.ASC, "id")));
        List<User> resultListOfUsers = users.getContent();
        if (resultListOfUsers.size() == 0) {
            return getErrorResponseBody(ApplicationErrorTypes.DB_IS_EMPTY_OR_PAGE_IS_NOT_EXIST);
        }
        int count = userService.countAll();
        return new ResponseEntity<>(convertUserList(resultListOfUsers, count), HttpStatus.OK);
    }

    private UsersDTO convertUserList(Collection<User> dbModel, Integer count) { return (dbModel == null) ? null : new UsersDTO(dbModel, count); }

    private UserDTO convert(User dbModel) {
        return (dbModel == null) ? null : new UserDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
