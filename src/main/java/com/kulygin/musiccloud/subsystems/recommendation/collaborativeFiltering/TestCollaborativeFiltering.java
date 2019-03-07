package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.impl.CSVFileFactory;

public class TestCollaborativeFiltering {
    public static void main(String[] args) {

        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        collaborativeFiltering.setSettings(new CSVFileFactory());

        long startTime = System.currentTimeMillis();

        collaborativeFiltering.makeRecommendation(1, 1000,500);

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Algorithm elapsed time: " + estimatedTime/1000d + "s");
    }
}
