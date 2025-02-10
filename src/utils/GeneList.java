package utils;

import components.Gene;

import java.util.ArrayList;

public class GeneList {

    public static ArrayList<Gene> fullGeneList = new ArrayList<>();

    public static void run() {
        // Contributes to death rate, compared against environmental temperature
        fullGeneList.add(new Gene(5, "temp"));
        // Determines reproduction rate
        fullGeneList.add(new Gene(50, "repRate"));
        // Determines reproductive age
        fullGeneList.add(new Gene(15, "repAge"));
    }
}
