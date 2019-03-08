package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ManhattanDistanseMeasure implements MeasureOfSimilarity {
    @Override
    public double calculate(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        return Math.abs(mDist(vector1, vector2));
    }

    private double mDist(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        AtomicReference<Double> d = new AtomicReference<>(0.0d);
        vector1.forEach((x, y) -> {
            if (vector2.containsKey(x)) {
                d.getAndUpdate(value -> value + (vector2.get(x) - y));
            }
        });
        return d.get();
    }
}
