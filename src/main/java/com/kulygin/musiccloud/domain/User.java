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
@Table
@EqualsAndHashCode(of = { "id"})
@ToString(exclude = {"userDetails", "userTracks", "playlists"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private LocalDateTime dateCreate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="user_details_id")
    private UserDetails userDetails;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Track> userTracks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Playlist> playlists;
}