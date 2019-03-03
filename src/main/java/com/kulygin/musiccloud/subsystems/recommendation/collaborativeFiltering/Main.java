package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.CSVFileReader;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl.CosineMeasure;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        CSVFileReader csvFileReader = new CSVFileReader();
        Map<Integer, Map<Integer, Integer>> userRates = csvFileReader.read("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\rcsData\\estimateMatrix.csv", ",");

        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        collaborativeFiltering.setMetric(new CosineMeasure());

        collaborativeFiltering.makeRecommendation(2, userRates, 5,5);
    }
}
