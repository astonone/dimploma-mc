package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.Dialog;
import com.kulygin.musiccloud.domain.Message;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.DialogHasNotExists;
import com.kulygin.musiccloud.exception.UserIsNotExistsException;
import com.kulygin.musiccloud.repository.DialogRepository;
import com.kulygin.musiccloud.repository.MessageRepository;
import com.kulygin.musiccloud.service.DialogService;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DialogServiceImpl implements DialogService {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;

    @Override
    public Dialog createDialog(String name, Long userId) throws UserIsNotExistsException {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Set<User> users = new HashSet<>();
        users.add(user);
        Dialog dialog = Dialog.builder()
                .name(name)
                .time(LocalDateTime.now())
                .users(users)
                .build();
        return dialogRepository.save(dialog);
    }

    @Override
    public List<Dialog> deleteDialog(Long dialogId) throws DialogHasNotExists {
        Dialog dialog = dialogRepository.getOne(dialogId);
        if (dialog == null) {
            throw new DialogHasNotExists();
        }
        dialogRepository.delete(dialog);
        return dialogRepository.findAll();
    }

    @Override
    public List<Dialog> findDialogs(Long userId) throws UserIsNotExistsException {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Set<User> users = new HashSet<>();
        users.add(user);
        return dialogRepository.findAllByUsersInOrderByTimeAsc(users);
    }

    @Override
    public Dialog addUserToDialog(Long dialogId, Long userId) throws UserIsNotExistsException, DialogHasNotExists {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Dialog dialog = dialogRepository.getOne(dialogId);
        if (dialog == null) {
            throw new DialogHasNotExists();
        }
        dialog.getUsers().add(user);
        return dialogRepository.save(dialog);
    }

    @Override
    public Dialog addMessageToDialog(Long dialogId, Long userId, String text) throws UserIsNotExistsException, DialogHasNotExists {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserIsNotExistsException();
        }
        Dialog dialog = dialogRepository.getOne(dialogId);
        if (dialog == null) {
            throw new DialogHasNotExists();
        }
        Message message = Message.builder()
                .text(text)
                .time(LocalDateTime.now())
                .userId(userId)
                .build();
        message = messageRepository.save(message);
        dialog.getMessages().add(message);
        return dialogRepository.save(dialog);
    }

    @Override
    public Dialog getDialog(Long dialogId) throws DialogHasNotExists {
        Dialog dialog = dialogRepository.getOne(dialogId);
        if (dialog == null) {
            throw new DialogHasNotExists();
        }
        return dialog;
    }

    @Override
    public Dialog startDialog(Long userOne, Long userTwo) throws UserIsNotExistsException {
        User user1 = userService.getUserById(userOne);
        if (user1 == null) {
            throw new UserIsNotExistsException();
        }
        User user2 = userService.getUserById(userTwo);
        if (user2 == null) {
            throw new UserIsNotExistsException();
        }
        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        List<Dialog> dialogs = dialogRepository.findByUsersInOrderByTimeAsc(users);
        Dialog dialog = findByUsers(dialogs, users);
        if (dialog == null) {
            dialog = dialogRepository.save(Dialog.builder()
                    .name(user1.getUserDetails().getLastName() + "," + user2.getUserDetails().getLastName())
                    .time(LocalDateTime.now())
                    .users(users)
                    .build());
        }
        return dialog;
    }

    private Dialog findByUsers(List<Dialog> dialogs, Set<User> users) {
        Dialog existedDialog = null;
        for (Dialog dialog : dialogs) {
            if (dialog.getUsers().equals(users)) {
                existedDialog = dialog;
                break;
            }
        }
        return existedDialog;
    }
}
