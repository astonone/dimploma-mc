package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity;

import java.util.Map;

public interface MeasureOfSimilarity {
    double calculate(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2);
}
