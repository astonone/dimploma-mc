package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class TanimotoСoefficientMeasure implements MeasureOfSimilarity {
    @Override
    public double calculate(Map<Integer, Integer> vector1, Map<Integer, Integer> vector2) {
        double a = vector1.size();
        double b = vector2.size();
        AtomicReference<Double> matches = new AtomicReference<>(0.0);

        vector1.forEach((x, y) -> {
            if (vector2.containsKey(x)) {
                matches.updateAndGet(v -> v + 1);
            }
        });

        double c = matches.get();
        return c / (a + b - c);
    }
}
