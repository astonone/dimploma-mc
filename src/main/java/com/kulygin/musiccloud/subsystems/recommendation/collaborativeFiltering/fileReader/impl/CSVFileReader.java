package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.impl;

import com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader.DataReader;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Log4j
public class CSVFileReader implements DataReader {
    /** key - userId, value - list of tracks rates : key - trackId, value - rate of track */
    public Map<Integer, Map<Integer, Integer>> read(String path, String separator) {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
        try {
            Files.readAllLines(Paths.get(path)).forEach(line -> {
                String[] vector = line.split(separator);

                Integer userId = Integer.parseInt(vector[0]);
                Integer trackId = Integer.parseInt(vector[1]);
                Integer rate = Integer.parseInt(vector[2]);

                if (result.containsKey(userId)) {
                    Map<Integer, Integer> trackRates = result.get(userId);
                    trackRates.put(trackId, rate);
                } else {
                    Map<Integer, Integer> trackRates = new HashMap<>();
                    trackRates.put(trackId, rate);
                    result.put(userId, trackRates);
                }
            });
        } catch (IOException e) {
            log.error("Error via csv file reading: ", e);
        }
        return result;
    }
}
