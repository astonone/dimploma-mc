package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Dialog;
import com.kulygin.musiccloud.exception.DialogHasNotExists;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;

import java.util.List;

public interface DialogService {
    Dialog createDialog(String name, Long userId) throws UserIsNotExistsException;

    List<Dialog> deleteDialog(Long dialogId) throws DialogHasNotExists;

    List<Dialog> findDialogs(Long userId) throws UserIsNotExistsException;

    Dialog addUserToDialog(Long dialogId, Long userId) throws UserIsNotExistsException, DialogHasNotExists;

    Dialog addMessageToDialog(Long dialogId, Long userId, String text) throws UserIsNotExistsException, DialogHasNotExists;

    Dialog getDialog(Long dialogId) throws DialogHasNotExists;

    Dialog startDialog(Long userOne, Long userTwo) throws UserIsNotExistsException;
}
