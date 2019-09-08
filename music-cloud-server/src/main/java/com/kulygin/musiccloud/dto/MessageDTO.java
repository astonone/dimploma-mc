package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private DateDTO time;
    private Long userId;

    public MessageDTO() {
    }

    public MessageDTO(Message message) {
        if (message == null) {
            return;
        }

        this.id = message.getId();
        this.text = message.getText();
        this.time = new DateDTO(message.getTime());
        this.userId = message.getUserId();
    }
}
