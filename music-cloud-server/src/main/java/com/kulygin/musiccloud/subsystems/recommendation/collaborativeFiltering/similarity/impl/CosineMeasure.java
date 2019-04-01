package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CosineMeasure implements MeasureOfSimilarity {
    @Override
    public double calculate(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        return (dotProduct(vector1, vector2) / Math.sqrt(dotProduct(vector1, vector1)) / Math.sqrt(dotProduct(vector2, vector2)));
    }

    private double dotProduct(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        AtomicReference<Double> d = new AtomicReference<>(0.0d);
        vector1.forEach((x, y) -> {
            if (vector2.containsKey(x)) {
                d.updateAndGet(value -> value + y * vector2.get(x));
            }
        });
        return d.get();
    }
}
