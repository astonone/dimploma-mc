package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Dialog;
import com.kulygin.musiccloud.domain.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
public class DialogDTO {
    private Long id;
    private String name;
    private DateDTO time;
    private List<UserDTO> users;
    private List<MessageDTO> messages;

    public DialogDTO() {
    }

    public DialogDTO(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        this.id = dialog.getId();
        this.name = dialog.getName();
        this.time = new DateDTO(dialog.getTime());

        this.users = ofNullable(dialog.getUsers()).orElse(newHashSet()).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());

        this.messages = ofNullable(dialog.getMessages()).orElse(newHashSet()).stream()
                .sorted(Comparator.comparing(Message::getTime))
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }
}
