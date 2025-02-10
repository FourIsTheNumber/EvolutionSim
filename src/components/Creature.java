package components;

import java.util.ArrayList;

import static utils.GeneList.fullGeneList;

public class Creature {
    // This genome will probably be later represented as a hashmap for fast lookup based on named genes
    ArrayList<Gene> genome = new ArrayList<>();

    int age = 20;

    // Base creature initializer
    public Creature() {
        for (Gene g : fullGeneList) {
            genome.add(g);
        }
        age = 0;
    }

    // Creature constructor which inherits genes
    public Creature(Creature parent) {
        for (Gene g : parent.genome) {
            genome.add(g.copy());
        }
        age = 0;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("    Creature\n");
        int i = 1;
        for (Gene g : genome) {
            s.append("Gene ").append(i).append(": ").append(g.value);
        }
        s.append("\nAge: ").append(age);
        return s.toString();
    }
}
