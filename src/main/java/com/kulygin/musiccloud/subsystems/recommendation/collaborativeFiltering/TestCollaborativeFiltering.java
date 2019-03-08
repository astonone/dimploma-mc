package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm.CollaborativeFiltering;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.CFilteringFactory;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.impl.CSVFileFactory;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl.CosineMeasure;

public class TestCollaborativeFiltering {
    public static void main(String[] args) {

        CollaborativeFiltering collaborativeFiltering = new CollaborativeFiltering();
        CFilteringFactory factory = new CSVFileFactory();
        collaborativeFiltering.setSettings(factory, false);

        long startTime = System.currentTimeMillis();

        collaborativeFiltering.makeRecommendation(1, 10000,1000);

        long estimatedTime = System.currentTimeMillis() - startTime;

        System.out.println("Algorithm elapsed time: " + estimatedTime/1000d + "s");

        //testAllMeasures(collaborativeFiltering, factory);
    }

    private static void calculationExperiment(CollaborativeFiltering collaborativeFiltering) {
        long startTime;
        long estimatedTime;
        long total = 0;
        int size = 100;

        for (int i = 0; i < size; i++) {
            startTime = System.currentTimeMillis();

            collaborativeFiltering.makeRecommendation(1, 100,100);

            estimatedTime = System.currentTimeMillis() - startTime;
            total += estimatedTime;
        }

        System.out.println("Algorithm elapsed time: " + total/size/1000d + "s");
    }

    /**
     * Preprocessing elapsed time: 12.686s
     * Cosine measure:
     * Algorithm elapsed time: 0.791s
     * EuclideanDistanseMeasure:
     * Algorithm elapsed time: 0.757s
     * ManhattanDistanseMeasure:
     * Algorithm elapsed time: 0.758s
     * PearsonСorrelationСoefficientMeasure:
     * Algorithm elapsed time: 0.792s
     * TanimotoСoefficientMeasure:
     * Algorithm elapsed time: 0.762s
     * */
    private static void testAllMeasures(CollaborativeFiltering collaborativeFiltering, CFilteringFactory factory) {
        factory.setMeasure(new CosineMeasure());
        System.out.println("Cosine measure: ");
        calculationExperiment(collaborativeFiltering);

        factory.setMeasure(new CosineMeasure());
        System.out.println("EuclideanDistanseMeasure: ");
        calculationExperiment(collaborativeFiltering);

        factory.setMeasure(new CosineMeasure());
        System.out.println("ManhattanDistanseMeasure: ");
        calculationExperiment(collaborativeFiltering);

        factory.setMeasure(new CosineMeasure());
        System.out.println("PearsonСorrelationСoefficientMeasure: ");
        calculationExperiment(collaborativeFiltering);

        factory.setMeasure(new CosineMeasure());
        System.out.println("TanimotoСoefficientMeasure: ");
        calculationExperiment(collaborativeFiltering);
    }
}
