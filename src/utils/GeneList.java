package utils;

import components.Gene;

import java.util.ArrayList;

public class GeneList {

    public static ArrayList<Gene> fullGeneList = new ArrayList<>();

    public static void run() {
        //TODO: some of these genes should probably be smaller numbers used to calculate the true value. Otherwise the
        // copying function needs to be more advanced

        // Contributes to death rate, compared against environmental temperature
        fullGeneList.add(new Gene(5, "temp", 1));
        // Determines reproduction rate
        fullGeneList.add(new Gene(50, "repRate", 0));
        // Determines reproductive age
        fullGeneList.add(new Gene(15, "repAge", 0));
    }
}
