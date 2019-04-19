package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.StatisticalAccounting;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.domain.User;
import com.kulygin.musiccloud.service.RecommendationService;
import com.kulygin.musiccloud.service.StatisticalAccountingService;
import com.kulygin.musiccloud.service.TrackService;
import com.kulygin.musiccloud.service.UserService;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.impl.DatabaseFactory;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator.GeneratorUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private StatisticalAccountingService statisticalAccountingService;
    @Autowired
    private CollaborativeFiltering collaborativeFiltering;
    @Autowired
    private DatabaseFactory databaseFactory;
    @Autowired
    private TrackService trackService;
    @Autowired
    private UserService userService;

    @Override
    public List<Track> getTracksForUser(Long userId, Integer nBestUsers, Integer nBestTracks) {
        collaborativeFiltering.setSettings(databaseFactory, false);

        log.info("Settings has been applied: ");

        return collaborativeFiltering.makeRecommendationAndGetTracks(userId.intValue(), nBestUsers, nBestTracks);
    }

    @Override
    public void fillDB(Integer size) {
        List<StatisticalAccounting> statisticalAccountings = new ArrayList<>();
        List<User> users = userService.findAll();
        List<Track> tracks = trackService.findAll();

        for (Integer i = 0; i < size; i++) {
            Integer userId = users.get(GeneratorUtils.rnd(0, users.size() - 1)).getId().intValue();
            Integer trackId = tracks.get(GeneratorUtils.rnd(0, tracks.size() - 1)).getId().intValue();
            Integer rate = GeneratorUtils.rnd(1,10);

            StatisticalAccounting statisticalAccounting = StatisticalAccounting.builder()
                    .userId(userId.longValue())
                    .trackId(trackId.longValue())
                    .ratingValue(rate)
                    .build();

            statisticalAccountings.add(statisticalAccounting);
        }

        statisticalAccountingService.saveAll(statisticalAccountings);
    }
}
