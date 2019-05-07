package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.dto.AllPlaylistWithTrackDTO;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.domain.Playlist;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.ErrorResponseBody;
import com.kulygin.musiccloud.dto.PlaylistDTO;
import com.kulygin.musiccloud.dto.PlaylistWithTrackDTO;
import com.kulygin.musiccloud.exception.PlaylistHasExistsException;
import com.kulygin.musiccloud.exception.PlaylistNotExistsException;
import com.kulygin.musiccloud.service.PlaylistService;
import com.kulygin.musiccloud.service.TrackService;
import com.kulygin.musiccloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/playlist")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackService trackService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPlaylist(@PathVariable("id") Long playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        if (playlist == null) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convert(playlist), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/showTracks", method = RequestMethod.GET)
    public ResponseEntity<?> getPlaylistWithTracks(@PathVariable("id") Long playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        if (playlist == null) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(convertPlaylistWithTrack(playlist), HttpStatus.OK);
    }

    @RequestMapping(value = "/all/showTracks", method = RequestMethod.GET)
    public ResponseEntity<?> getAllPlaylistsWithTracksByUser(@RequestParam("userId") Long userId) {
        List<Playlist> playlist = playlistService.getAllPlaylistsWithTracks(userId);
        return new ResponseEntity<>(convertPlaylistsWithTrack(playlist), HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createPlaylist(@RequestParam("name") String name, @RequestParam("userId") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        Playlist playlist = null;
        try {
            playlist = playlistService.createPlaylist(name, user);
        } catch (PlaylistHasExistsException playlistHasExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_HAS_EXISTS);
        }
        return new ResponseEntity<>(convert(playlist), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePlaylist(@PathVariable("id") Long playlistId) {
        try {
            playlistService.deletePlaylist(playlistId);
        } catch (PlaylistNotExistsException playlistNotExistsException) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_ID_NOT_FOUND);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/track", method = RequestMethod.PUT)
    public ResponseEntity<?> addTrackInPlaylist(@PathVariable("id") Long playlistId, @RequestParam("trackId") Long trackId) {

        Playlist playlist = playlistService.getPlaylistById(playlistId);
        if (playlist == null) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_ID_NOT_FOUND);
        }
        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return getErrorResponseBody(ApplicationErrorTypes.TRACK_ID_NOT_FOUND);
        }
        playlist = playlistService.addTrackInPlaylist(playlist, track);
        return new ResponseEntity<>(convertPlaylistWithTrack(playlist), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}/track", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeTrackInPlaylist(@PathVariable("id") Long playlistId, @RequestParam("trackId") Long trackId) {

        Playlist playlist = playlistService.getPlaylistById(playlistId);
        if (playlist == null) {
            return getErrorResponseBody(ApplicationErrorTypes.PLAYLIST_ID_NOT_FOUND);
        }
        Track track = trackService.getTrackById(trackId);
        if (track == null) {
            return getErrorResponseBody(ApplicationErrorTypes.TRACK_ID_NOT_FOUND);
        }
        playlist = playlistService.removeTrackInPlaylist(playlist, track);
        return new ResponseEntity<>(convertPlaylistWithTrack(playlist), HttpStatus.OK);
    }

    private PlaylistDTO convert(Playlist dbModel) {
        return (dbModel == null) ? null : new PlaylistDTO(dbModel);
    }

    private PlaylistWithTrackDTO convertPlaylistWithTrack(Playlist dbModel) {
        return (dbModel == null) ? null : new PlaylistWithTrackDTO(dbModel);
    }

    private AllPlaylistWithTrackDTO convertPlaylistsWithTrack(List<Playlist> dbModel) {
        return (dbModel == null) ? null : new AllPlaylistWithTrackDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
