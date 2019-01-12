package com.kulygin.musiccloud.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@EqualsAndHashCode(of = { "id"})
@ToString(exclude = {"moods", "genres", "playlists", "users", "sumOfRatings", "countOfRate"})
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String artist;
    private String album;
    private Integer year;
    private String filename;
    private String duration;
    private Double rating;

    private Long sumOfRatings;
    private Long countOfRate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "genres_of_tracks",
            joinColumns = {@JoinColumn(name = "track_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "moods_of_tracks",
            joinColumns = {@JoinColumn(name = "track_id")},
            inverseJoinColumns = {@JoinColumn(name = "mood_id")})
    private Set<Mood> moods;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tracks_by_playlist",
            joinColumns = {@JoinColumn(name = "track_id")},
            inverseJoinColumns = {@JoinColumn(name = "playlist_id")})
    private Set<Playlist> playlists;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tracks_by_users",
            joinColumns = {@JoinColumn(name = "track_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;
}

