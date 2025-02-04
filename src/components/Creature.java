package components;

import java.util.ArrayList;

public class Creature {
    // This genome will probably be later represented as a hashmap for fast lookup based on named genes
    ArrayList<Gene> genome = new ArrayList<>();

    int age = 0;

    // Test default
    public Creature(int temp) {
        genome.add(new Gene(temp));
    }

    public Creature(Creature parent) {
        for (Gene g : parent.genome) {
            genome.add(g.copy());
        }
        age = 0;
    }
}
