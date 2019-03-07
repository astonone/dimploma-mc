package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.Track;

import java.util.List;

public interface RecommendationService {
    List<Track> getTracksForUser(Long userId, Integer nBestUsers, Integer nBestTracks);
    void fillDB();
}
