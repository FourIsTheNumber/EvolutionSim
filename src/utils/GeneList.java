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
        fullGeneList.add(new Gene(20, ReproductiveAge));
        // Determines reproduction rate
        fullGeneList.add(new Gene(50, ReproductiveRate));
        // Special gene determining ability to survive in water.
        // At any value from 6-10, creatures die instantly in aquatic biomes and will not enter them willingly.
        // At any value from 0-4, creatures die instantly in land biomes and will not enter them willingly.
        // At value 5, creatures can enter either biome but take penalties.
        fullGeneList.add(new Gene(10, Aquatic));
    }
}
