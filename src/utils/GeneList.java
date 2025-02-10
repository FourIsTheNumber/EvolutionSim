package utils;

import components.Gene;

import java.util.ArrayList;

public class GeneList {

    public static ArrayList<Gene> fullGeneList = new ArrayList<>();

    public static void run() {
        fullGeneList.add(new Gene(5, "Temperature"));
    }
}
