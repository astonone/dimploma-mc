package com.kulygin.musiccloud.web;

import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.dto.*;
import com.kulygin.musiccloud.enumeration.ApplicationErrorTypes;
import com.kulygin.musiccloud.service.RecommendationService;
import com.kulygin.musiccloud.service.UserService;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator.PeopleGenerator;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator.TrackGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/recommend")
public class RecommendationController {

    @Autowired
    private UserService userService;
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private PeopleGenerator peopleGenerator;
    @Autowired
    private TrackGenerator trackGenerator;

    @RequestMapping(value = "/tracksForUser/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTracksForUser(@PathVariable("id") Long userId, @PathParam("nBestUsers") Integer nBestUsers, @PathParam("nBestTracks") Integer nBestTracks) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return getErrorResponseBody(ApplicationErrorTypes.USER_ID_NOT_FOUND);
        }
        List<Track> tracksForUser = recommendationService.getTracksForUser(userId, nBestUsers, nBestTracks);
        return new ResponseEntity<>(convert(tracksForUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/fillStatistics", method = RequestMethod.GET)
    public ResponseEntity<?> fillDB(@PathParam("count") Integer size) {
        recommendationService.fillDB(size);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(value = "/generatePeople", method = RequestMethod.GET)
    public ResponseEntity<?> generatePeople(@PathParam("count") Integer count) {
        List<User> users = peopleGenerator.generateUsers(count);
        return new ResponseEntity<>(convertUserList(users),HttpStatus.OK);
    }

    @RequestMapping(value = "/generateTracks", method = RequestMethod.GET)
    public ResponseEntity<?> generateTracks(@PathParam("count") Integer count) {
        List<Track> tracks = trackGenerator.generateTracks(count);
        return new ResponseEntity<>(convert(tracks),HttpStatus.OK);
    }

    private AllUsersDTO convertUserList(List<User> dbModel) { return (dbModel == null) ? null : new AllUsersDTO(dbModel); }

    private AllTracksDTO convert(List<Track> dbModel) {
        return (dbModel == null) ? null : new AllTracksDTO(dbModel);
    }

    private ResponseEntity<ErrorResponseBody> getErrorResponseBody(ApplicationErrorTypes errorType) {
        return new ResponseEntity<>(new ErrorResponseBody(errorType), HttpStatus.NOT_FOUND);
    }
}
