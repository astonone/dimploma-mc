package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.domain.Dialog;
import com.kulygin.musiccloud.dto.AllDialogsDTO;
import com.kulygin.musiccloud.dto.DialogDTO;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.exception.DialogHasNotExists;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;
import com.kulygin.musiccloud.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dialog")
public class DialogController {

    @Autowired
    private DialogService dialogService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createDialog(@RequestParam("name") String name, @RequestParam("user") Long userId) {
        Dialog dialog = null;
        try {
            dialog = dialogService.createDialog(name, userId);
        } catch (UserIsNotExistsException e) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(dialog), HttpStatus.OK);
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity<?> startDialog(@RequestParam("userOne") Long userOne, @RequestParam("userTwo") Long userTwo) {
        Dialog dialog = null;
        try {
            dialog = dialogService.startDialog(userOne, userTwo);
        } catch (UserIsNotExistsException e) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(dialog), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDialog(@PathVariable("id") Long dialogId) {
        try {
            Dialog dialog = dialogService.getDialog(dialogId);
            return new ResponseEntity<>(convert(dialog), HttpStatus.OK);
        } catch (DialogHasNotExists dialogHasNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.INVALID_DATA);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDialog(@PathVariable("id") Long dialogId) {
        try {
            List<Dialog> dialogs = dialogService.deleteDialog(dialogId);
            return new ResponseEntity<>(convert(dialogs, dialogs.size()), HttpStatus.OK);
        } catch (DialogHasNotExists dialogHasNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.INVALID_DATA);
        }
    }

    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findDialogs(@PathVariable("id") Long userId) {
        try {
            List<Dialog> dialogs = dialogService.findDialogs(userId);
            return new ResponseEntity<>(convert(dialogs, dialogs.size()), HttpStatus.OK);
        } catch (UserIsNotExistsException e) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/addUser/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToDialog(@PathVariable("id") Long dialogId, @RequestParam("userId") Long userId) {
        try {
            Dialog dialog = dialogService.addUserToDialog(dialogId, userId);
            return new ResponseEntity<>(convert(dialog), HttpStatus.OK);
        } catch (UserIsNotExistsException e) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (DialogHasNotExists dialogHasNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.INVALID_DATA);
        }
    }

    @RequestMapping(value = "/addMessage/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addUserToDialog(@PathVariable("id") Long dialogId, @RequestParam("text") String text, @RequestParam("userId") Long userId) {
        try {
            Dialog dialog = dialogService.addMessageToDialog(dialogId, userId, text);
            return new ResponseEntity<>(convert(dialog), HttpStatus.OK);
        } catch (UserIsNotExistsException e) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        } catch (DialogHasNotExists dialogHasNotExists) {
            return getErrorResponseBody(ApplicationErrorTypes.INVALID_DATA);
        }
    }

    private DialogDTO convert(Dialog dbModel) {
        return (dbModel == null) ? null : new DialogDTO(dbModel);
    }

    private AllDialogsDTO convert(List<Dialog> dbModel, int count) {
        return (dbModel == null) ? null : new AllDialogsDTO(dbModel, count);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
