package utils;

import components.Creature;
import components.Environment;
import components.Gene;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static utils.GeneList.fullGeneList;

/**
 * Handles writing csv file
 */
public class Logger {
    public static final PrintWriter csvWriter;

    public static final boolean LOG_EVERY_CREATURE = false;
    public static final boolean LOG_EMPTY_ENVIRONMENTS = false;

    static {
        try {
            csvWriter = new PrintWriter(new FileWriter("src/outputs/simulation_data.csv"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeHeader() {
        List<String> header = new ArrayList<>();

            header.add("Year");
            header.add("Environment #");
            header.add("Environment Biome");
            header.add("Environment Temperature");
        if (LOG_EVERY_CREATURE) {
            header.add("Age");
            header.add("Food Usage");
            for (Gene g : fullGeneList) header.add(g.data.name());
        } else {
            header.add("Population");
            for (Gene g : fullGeneList) header.add(g.data.name() + "(Average)");
        }
        csvWriter.println(String.join(",", header));
    }

    public static void logCreature(Environment e, Creature c, int y) {
        List<String> row = new ArrayList<>();

        row.add(Integer.toString(y));
        row.add(Integer.toString(e.number));
        row.add(e.getBiome().name);
        row.add(Integer.toString(e.getTemperature()));
        row.add(Integer.toString(c.age));
        row.add(Integer.toString(c.foodUse));
        for (Gene g : c.genome.values()) row.add(Integer.toString(g.value));
        csvWriter.println(String.join(",", row));
    }

    public static void logEnvironment(Environment e, int y) {
        List<String> row = new ArrayList<>();

        row.add(Integer.toString(y));
        row.add(Integer.toString(e.number));
        row.add(e.getBiome().name);
        row.add(Integer.toString(e.getTemperature()));
        row.add(Integer.toString(e.creatures.size()));
        for (Gene g : fullGeneList) row.add(String.format("%.2f", e.getAverageGene(g.data.key)));
        csvWriter.println(String.join(",", row));
    }
}
