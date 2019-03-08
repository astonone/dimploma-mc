package com.kulygin.musiccloud.subsystems.recommendation.collaborativeFiltering.generator;

import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class CSVFileGenerator {

    private int userCounts;
    private int productCounts;
    private int matrixSize;

    public CSVFileGenerator(int userCounts, int productCounts, int matrixSize) {
        this.userCounts = userCounts;
        this.productCounts = productCounts;
        this.matrixSize = matrixSize;
    }

    public void writeToCSV(String path, String separator) {

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < matrixSize; i++) {
            lines.add(GeneratorUtils.rnd(1, userCounts) + separator + GeneratorUtils.rnd(1, productCounts) + separator + GeneratorUtils.rnd(1, 10));
        }

        Path file = Paths.get(path);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            log.error("Error via file writing: ", e);
        }
    }

    public static void main(String[] args) {
        CSVFileGenerator testDataGenerator = new CSVFileGenerator(10000, 10000, 10000000);
        testDataGenerator.writeToCSV("C:\\Users\\aston\\IdeaProjects\\my projects\\dimploma-mc\\rcsData\\estimateMatrix.csv", ",");
    }
}
