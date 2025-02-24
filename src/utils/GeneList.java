package utils;

import components.Gene;

import java.util.ArrayList;

import static components.GenePacket.*;

public class GeneList {

    public static ArrayList<Gene> fullGeneList = new ArrayList<>();

    public static void run() {
        //TODO: some of these genes should probably be smaller numbers used to calculate the true value. Otherwise the
        // copying function needs to be more advanced

        // Contributes to death rate, compared against environmental temperature
        fullGeneList.add(new Gene(5, Temperature));
        // Determines reproductive age
        fullGeneList.add(new Gene(15, ReproductiveAge));
        // Determines reproduction rate
        fullGeneList.add(new Gene(50, ReproductiveRate));
    }
}
