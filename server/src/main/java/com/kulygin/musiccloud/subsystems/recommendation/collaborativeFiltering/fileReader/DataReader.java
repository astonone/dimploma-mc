package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.fileReader;

import java.util.Map;

public interface DataReader {
    Map<Integer, Map<Integer, Integer>> read(String path, String separator);
}
