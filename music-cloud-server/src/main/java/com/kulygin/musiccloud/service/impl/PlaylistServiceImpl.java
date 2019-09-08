package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.Playlist;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.PlaylistHasExistsException;
import com.kulygin.musiccloud.exception.PlaylistNotExistsException;
import com.kulygin.musiccloud.repository.PlaylistRepository;
import com.kulygin.musiccloud.repository.TrackRepository;
import com.kulygin.musiccloud.service.PlaylistService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
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
            log.error("Playlist has not found: " + name);
            throw new PlaylistHasExistsException();
        }
        Playlist newPlaylist = Playlist.builder()
                .name(name)
                .user(user)
                .build();
        return playlistRepository.save(newPlaylist);
    }

    @Override
    public Playlist addTrackInPlaylist(Playlist playlist, Track track) {
        track.getPlaylists().add(playlist);
        trackRepository.save(track);
        playlist.getTracks().add(track);
        return playlist;
    }

    @Override
    public void deletePlaylist(Long id) throws PlaylistNotExistsException {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistNotExistsException();
        }
        playlistRepository.deleteById(id);
    }

    @Override
    public List<Playlist> getAllPlaylistsWithTracks(Long userId) {
        return playlistRepository.findAllByUser_IdOrderByNameAsc(userId);
    }

    @Override
    public Playlist removeTrackInPlaylist(Playlist playlist, Track track) {
        track.getPlaylists().remove(playlist);
        trackRepository.save(track);
        playlist.getTracks().remove(track);
        return playlist;
    }
}
