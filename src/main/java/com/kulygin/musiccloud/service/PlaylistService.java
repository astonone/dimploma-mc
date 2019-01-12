package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Playlist;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.exception.PlaylistHasExistsException;
import com.kulygin.musiccloud.exception.PlaylistNotExistsException;

public interface PlaylistService {

    Playlist getPlaylistById(Long id);

    Playlist createPlaylist(String name, User user) throws PlaylistHasExistsException;

    void addTrackInPlaylist(Playlist tracklist, Track track);

    void deletePlaylist(Long id) throws PlaylistNotExistsException;
}
