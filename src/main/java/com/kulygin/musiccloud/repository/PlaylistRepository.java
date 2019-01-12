package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.Playlist;
import com.kulygin.musiccloud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findByNameAndUser(String name, User user);
}
