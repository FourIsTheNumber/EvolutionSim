package utils;

import components.Biome;
import components.Environment;

public class CreatureSimulator {

    // This is a testing file used to determine the likelihood of a single creature reaching reproductive age

    private static final int TRIALS = 100000;
    private static final int YEARS = 25;

    public static void main(String[] args) {
        GeneList.run();

        int successes = 0;

        for (int t = 0; t < TRIALS; t++) {

            Environment env = new Environment(10, 1000, Biome.Forest);
            env.addCreatures(1);

            for (int i = 0; i < YEARS; i++) {
                env.simulateYear(i);
            }

            if (!env.creatures.isEmpty()) successes++;
        }

        System.out.println("Simulated " + YEARS + " years across " + TRIALS + " trials!");
        System.out.println("Successes: " + successes + ". Success rate: " + 100 * (float) successes / TRIALS + "%");
    }
}
