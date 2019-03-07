package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.StatisticalAccounting;
import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.service.RecommendationService;
import com.kulygin.musiccloud.service.StatisticalAccountingService;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.impl.DatabaseFactory;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private StatisticalAccountingService statisticalAccountingService;
    @Autowired
    private CollaborativeFiltering collaborativeFiltering;
    @Autowired
    private DatabaseFactory databaseFactory;

    @Override
    public List<Track> getTracksForUser(Long userId, Integer nBestUsers, Integer nBestTracks) {
        Map<Integer, Map<Integer, Integer>> userRates = new HashMap<>();

        collaborativeFiltering.setSettings(databaseFactory);

        return collaborativeFiltering.makeRecommendationAndGetTracks(userId.intValue(), nBestUsers, nBestTracks);
    }

    @Override
    public void fillDB() {
        List<StatisticalAccounting> statisticalAccountings = new ArrayList<>();
        try {
            Files.readAllLines(Paths.get("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\rcsData\\estimateMatrix.csv")).forEach(line -> {

                String[] vector = line.split(",");

                Integer userId = Integer.parseInt(vector[0]);
                Integer trackId = Integer.parseInt(vector[1]);
                Integer rate = Integer.parseInt(vector[2]);

                StatisticalAccounting statisticalAccounting = StatisticalAccounting.builder()
                        .userId(userId.longValue())
                        .trackId(trackId.longValue())
                        .ratingValue(rate)
                        .build();

                statisticalAccountings.add(statisticalAccounting);
            });
            statisticalAccountingService.saveAll(statisticalAccountings);
        } catch (IOException e) {
            log.error("Error via csv file reading: ", e);
        }
    }
}
