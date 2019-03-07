package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;

import java.util.Map;

public abstract class CFilteringFactory {
    public abstract Map<Integer, Map<Integer, Integer>> createData();
    public abstract MeasureOfSimilarity createMeasureOfSimilarity();
}
