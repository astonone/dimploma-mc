package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.algorythm;

import com.kulygin.musiccloud.domain.Track;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.config.CFilteringFactory;
import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.similarity.MeasureOfSimilarity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Component
public class CollaborativeFiltering {
    private MeasureOfSimilarity metric;
    private Map<Integer, Map<Integer, Integer>> userRates;

    public void setSettings(CFilteringFactory cFilteringFactory) {
        this.metric = cFilteringFactory.createMeasureOfSimilarity();
        this.userRates = cFilteringFactory.createData();
    }

    public void makeRecommendation(Integer userID, Integer nBestUsers, Integer nBestProducts) {
        Map<Integer, Double> matches = new HashMap<>();
        /** Choose L users whose tastes are most similar to the tastes of the subject.
         To do this, for each user, you need to calculate the selected measure in relation to
         the user, and choose L largest */

        userRates.forEach((user, value) -> {
            if (!user.equals(userID)) {
                matches.put(user, metric.calculate(userRates.get(userID), userRates.get(user)));
            }
        });

        System.out.println("Most correlated '" + userID + "' with users:");
        matches.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(nBestUsers)
                .forEach(match -> System.out.println("  UserID: " + match.getKey() + "  Coefficient: " + match.getValue()));

        Map<Integer, Double> bestMatches = matches.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(nBestUsers)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** For each user multiply his estimates by the calculated value of the measure,
         * thus, the evaluation of more "similar" users will have a stronger impact on the final
         * position of the product. For each of the products calculate the sum of the calibrated estimates L
         * * the closest users received amount divided by the amount of measures L selected users. */
        Map<Integer, Double> similarityProducts = new HashMap<>();
        double sum = bestMatches.entrySet().stream().flatMapToDouble(match -> DoubleStream.of(match.getValue())).sum();

        final Map<Integer, Double> resultBestMatches = bestMatches.entrySet().stream()
                .filter(match -> match.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        resultBestMatches.forEach((relatedUser, relatedUserValue) ->
                userRates.get(relatedUser).forEach((product, productValue) -> {
                    if (!userRates.get(userID).containsKey(product)) {
                        similarityProducts.putIfAbsent(product, 0.0d);
                        similarityProducts.put(product, (similarityProducts.get(product) + (userRates.get(relatedUser).get(product) * resultBestMatches.get(relatedUser))) / sum);
                    }
                }));

        System.out.println("Most correlated products: ");
        similarityProducts.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(nBestProducts)
                .forEach(similarityProduct -> System.out.println("  TrackID: " + similarityProduct.getKey() + "  Correlation coefficient: " + similarityProduct.getValue()));
    }

    public List<Track> makeRecommendationAndGetTracks(Integer userID, Integer nBestUsers, Integer nBestProducts) {
        Map<Integer, Double> matches = new HashMap<>();
        /** Choose L users whose tastes are most similar to the tastes of the subject.
         To do this, for each user, you need to calculate the selected measure in relation to
         the user, and choose L largest */

        userRates.forEach((user, value) -> {
            if (!user.equals(userID)) {
                matches.put(user, metric.calculate(userRates.get(userID), userRates.get(user)));
            }
        });

        Map<Integer, Double> bestMatches = matches.entrySet().stream()
                .limit(nBestUsers)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        /** For each user multiply his estimates by the calculated value of the measure,
         * thus, the evaluation of more "similar" users will have a stronger impact on the final
         * position of the product. For each of the products calculate the sum of the calibrated estimates L
         * * the closest users received amount divided by the amount of measures L selected users. */
        Map<Integer, Double> similarityProducts = new HashMap<>();
        double sum = bestMatches.entrySet().stream().flatMapToDouble(match -> DoubleStream.of(match.getValue())).sum();

        final Map<Integer, Double> resultBestMatches = bestMatches.entrySet().stream()
                .filter(match -> match.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        resultBestMatches.forEach((relatedUser, relatedUserValue) ->
                userRates.get(relatedUser).forEach((product, productValue) -> {
                    if (!userRates.get(userID).containsKey(product)) {
                        similarityProducts.putIfAbsent(product, 0.0d);
                        similarityProducts.put(product, (similarityProducts.get(product) + (userRates.get(relatedUser).get(product) * resultBestMatches.get(relatedUser))) / sum);
                    }
                }));

        return new ArrayList<>();
    }
}