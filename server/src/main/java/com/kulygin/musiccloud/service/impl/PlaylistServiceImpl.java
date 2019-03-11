package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.Playlist;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.PlaylistHasExistsException;
import com.kulygin.musiccloud.exception.PlaylistNotExistsException;
import com.kulygin.musiccloud.repository.PlaylistRepository;
import com.kulygin.musiccloud.repository.TrackRepository;
import com.kulygin.musiccloud.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private TrackRepository trackRepository;

    @Override
    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    @Override
    public Playlist createPlaylist(String name, User user) throws PlaylistHasExistsException {
        Playlist playlist = playlistRepository.findByNameAndUser(name, user);
        if (playlist != null) {
            throw new PlaylistHasExistsException();
        }
        Playlist newPlaylist = Playlist.builder()
                .name(name)
                .user(user)
                .build();
        return playlistRepository.save(newPlaylist);
    }

    @Override
    public void addTrackInPlaylist(Playlist playlist, Track track) {
        track.getPlaylists().add(playlist);
        trackRepository.save(track);
    }

    @Override
    public void deletePlaylist(Long id) throws PlaylistNotExistsException {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistNotExistsException();
        }
        playlistRepository.deleteById(id);
    }
}
