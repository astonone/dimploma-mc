package com.kulygin.musiccloud.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "public")
@EqualsAndHashCode(of = {"id"})
@ToString(exclude = {"userDetails", "userTracks", "playlists", "friends", "friendRequests", "userDialogs"})
public class User implements org.springframework.security.core.userdetails.UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private LocalDateTime dateCreate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Track> userTracks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Playlist> playlists;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Dialog> userDialogs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friend_requests",
            joinColumns = @JoinColumn(name = "inviter_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> friendRequests;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
