package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.CSVFileReader;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl.CosineMeasure;

import java.util.Map;

public class TestCollaborativeFiltering {
    public static void main(String[] args) {
        CSVFileReader csvFileReader = new CSVFileReader();
        Map<Integer, Map<Integer, Integer>> userRates = csvFileReader.read("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\rcsData\\estimateMatrix.csv", ",");

        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        collaborativeFiltering.setMetric(new CosineMeasure());

        long startTime = System.currentTimeMillis();

        collaborativeFiltering.makeRecommendation(2, userRates, 5,5);

        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time: " + estimatedTime/1000d + "s");
    }
}
