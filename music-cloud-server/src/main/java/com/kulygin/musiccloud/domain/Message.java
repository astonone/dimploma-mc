package com.kulygin.musiccloud.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public")
@EqualsAndHashCode(of = { "id"})
@ToString(exclude = {"dialogs"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    private LocalDateTime time;
    private Long userId;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "messages")
    private Set<Dialog> dialogs;
}
