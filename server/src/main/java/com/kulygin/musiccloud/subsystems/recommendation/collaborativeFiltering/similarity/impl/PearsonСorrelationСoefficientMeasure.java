package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PearsonСorrelationСoefficientMeasure implements MeasureOfSimilarity {
    @Override
    public double calculate(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        return (vDiff(vector1, average(vector1)) * vDiff(vector2, average(vector2))) /
                Math.sqrt(vDiff(vector1, average(vector1)) * vDiff(vector1, average(vector1))
                        * vDiff(vector2, average(vector2)) * vDiff(vector2, average(vector2)));
    }

    private Map<Integer, Integer> average(Map<Integer, Integer> vector) {
        Map<Integer, Integer> newVector = new HashMap<>();
        vector.forEach((x, y) -> newVector.put(x, (1 / vector.size()) * y));
        return newVector;
    }

    private double vDiff(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        AtomicReference<Double> d = new AtomicReference<>(0.0d);
        vector1.forEach((x, y) -> {
            if (vector2.containsKey(x)) {
                d.getAndUpdate(value -> value + (vector2.get(x) - y));
            }
        });
        return d.get();
    }
}
