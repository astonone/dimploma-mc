package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.CFilteringFactory;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.impl.CSVFileReader;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl.CosineMeasure;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CSVFileFactory extends CFilteringFactory {
    @Override
    public Map<Integer, Map<Integer, Integer>> createData() {
        long startTime = System.currentTimeMillis();
        Map<Integer, Map<Integer, Integer>> data = new CSVFileReader().read("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\additional\\rcsData\\estimateMatrix.csv", ",");
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("Preprocessing elapsed time: " + estimatedTime/1000d + "s");
        return data;
    }

    @Override
    public MeasureOfSimilarity createMeasureOfSimilarity() {
        return new CosineMeasure();
    }
}
