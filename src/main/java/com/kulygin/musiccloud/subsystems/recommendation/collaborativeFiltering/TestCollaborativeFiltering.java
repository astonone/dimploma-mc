package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.CSVFileReader;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl.CosineMeasure;

import java.util.Map;

public class TestCollaborativeFiltering {
    public static void main(String[] args) {
        CSVFileReader csvFileReader = new CSVFileReader();

        long startTime = System.currentTimeMillis();

        Map<Integer, Map<Integer, Integer>> userRates = csvFileReader.read("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\rcsData\\estimateMatrix.csv", ",");

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Preprocessing elapsed time: " + estimatedTime/1000d + "s");

        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        collaborativeFiltering.setMetric(new CosineMeasure());

        startTime = System.currentTimeMillis();

        collaborativeFiltering.makeRecommendation(1, userRates, 1000,500);

        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Algorithm elapsed time: " + estimatedTime/1000d + "s");
    }
}
